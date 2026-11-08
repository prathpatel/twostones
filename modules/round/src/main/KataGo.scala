package lila.round

import play.api.libs.json.*

import java.io.{ BufferedReader, InputStreamReader, OutputStreamWriter, Writer }
import java.nio.charset.StandardCharsets.UTF_8
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.{ ConcurrentHashMap, ConcurrentLinkedDeque, Executors, TimeUnit, TimeoutException }
import scala.jdk.CollectionConverters.*
import scala.util.Try

import goban.{ Game, Handicap, Move, Point, Stone }

/** KataGo's analysis engine (`katago analysis`): one process answers every game at once.
  *
  * Each request is one line of JSON describing a whole position; answers come back as lines
  * of JSON, in any order, matched by their id. KataGo searches up to `numAnalysisThreads`
  * positions in parallel (conf/katago.cfg) and batches their network evaluations, so many
  * people can play the computer at the same time. The server picks the move itself from the
  * answer (see [[KataGo.choose]]).
  * https://github.com/lightvector/KataGo/blob/master/docs/Analysis_Engine.md
  */
object KataGo:

  case class Error(message: String) extends Exception(message)

  /** A line-based connection to an engine: JSON queries in, JSON answers out. */
  trait Connection:
    def send(line: String): Unit

    /** The next line, blocking; None once the engine has closed the connection. */
    def readLine(): Option[String]
    def close(): Unit

    /** The last lines the engine wrote to its error output: why it stopped. */
    def lastErrors: String = ""

  object Connection:

    /** Launch the engine process and talk to it on stdin/stdout. */
    def process(command: List[String], workDir: Option[java.io.File]): Connection =
      val builder = ProcessBuilder(command*)
      workDir.foreach(builder.directory)
      val proc = builder.start()
      // read stderr all the time (a full pipe would block KataGo), keeping the last lines
      val errors = ConcurrentLinkedDeque[String]()
      val errorReader = daemon("katago-stderr"):
        val err = BufferedReader(InputStreamReader(proc.getErrorStream, UTF_8))
        Try:
          var line = err.readLine()
          while line != null do
            errors.addLast(line)
            while errors.size > 5 do errors.pollFirst()
            line = err.readLine()
      streams(
        BufferedReader(InputStreamReader(proc.getInputStream, UTF_8)),
        OutputStreamWriter(proc.getOutputStream, UTF_8),
        () => proc.destroy(),
        () =>
          // asked once its output ended: let it exit, and its last words arrive
          Try(proc.waitFor(1, TimeUnit.SECONDS))
          Try(errorReader.join(1000))
          errors.asScala.mkString(" / ")
      )

    def streams(
        in: BufferedReader,
        out: Writer,
        onClose: () => Unit = () => (),
        errors: () => String = () => ""
    ): Connection = new:
      def send(line: String): Unit =
        out.write(line)
        out.write("\n")
        out.flush()
      def readLine(): Option[String] = Option(in.readLine())
      def close(): Unit =
        Try(out.close())
        onClose()
      override def lastErrors = errors()

  /** One engine process shared by all games, started on the first query and restarted
    * after it stops or stops answering. Thread safe.
    */
  final class Engine(connect: () => Connection, timeout: FiniteDuration):

    private val ids = AtomicLong()

    /** A running engine and the queries it has not answered yet. */
    final private class Live(val conn: Connection):
      val pending = ConcurrentHashMap[String, Promise[JsObject]]()
      @volatile var lastRead: Long = System.nanoTime()
      @volatile var stopped = false
      daemon("katago-reader"):
        try
          var line = conn.readLine()
          while line.isDefined do
            lastRead = System.nanoTime()
            line.foreach(answer)
            line = conn.readLine()
        catch case _: Exception => ()
        val why = conn.lastErrors
        stop(Live.this, s"KataGo stopped${if why.isEmpty then "" else s": $why"}")

      def answer(line: String): Unit =
        Try(Json.parse(line)).toOption match
          case Some(o: JsObject) =>
            val id = (o \ "id").asOpt[String]
            val error = (o \ "error").asOpt[String]
            if o.keys.contains("warning") then logger.warn(s"KataGo: $line")
            // partial results, only sent when a query asks for them
            else if (o \ "isDuringSearch").asOpt[Boolean].contains(true) then ()
            else
              id.flatMap(i => Option(pending.remove(i))) match
                case Some(promise) =>
                  error match
                    case Some(e) => promise.tryFailure(Error(e))
                    case None => promise.trySuccess(o)
                  ()
                // answers to queries that already timed out, and acknowledgements of "terminate"
                case None => error.foreach(e => logger.warn(s"KataGo: $e"))
          case _ => logger.debug(s"KataGo, not JSON: $line")

    private var live: Option[Live] = None // guarded by this

    private def current(): Live = synchronized:
      live.filterNot(_.stopped) match
        case Some(l) => l
        case None =>
          val l = Live(connect())
          live = Some(l)
          l

    private def stop(l: Live, reason: String): Unit =
      l.stopped = true
      synchronized:
        if live.contains(l) then live = None
      Try(l.conn.close())
      val error = Error(reason)
      l.pending.values.asScala.foreach(_.tryFailure(error))
      l.pending.clear()

    /** Send a query (without its id) and wait for its answer, at most `timeout`. */
    def query(q: JsObject): Fu[JsObject] =
      val id = ids.incrementAndGet().toString
      val promise = Promise[JsObject]()
      Try(current()) match
        case scala.util.Failure(e) =>
          promise.tryFailure(Error(s"KataGo could not start: ${e.getMessage}"))
        case scala.util.Success(l) =>
          l.pending.put(id, promise)
          val sentAt = System.nanoTime()
          try
            l.synchronized(l.conn.send(Json.stringify(q + ("id" -> JsString(id)))))
            Engine.timer.schedule((() => timedOut(l, id, sentAt)): Runnable, timeout.toMillis, TimeUnit.MILLISECONDS)
            // it stopped just as the query went in: nobody will answer it
            if l.stopped && l.pending.remove(id) != null then promise.tryFailure(Error("KataGo stopped"))
          catch
            case e: Exception =>
              stop(l, s"KataGo stopped: ${e.getMessage}")
              promise.tryFailure(e)
      promise.future

    private def timedOut(l: Live, id: String, sentAt: Long): Unit =
      Option(l.pending.remove(id)).foreach: promise =>
        if l.lastRead - sentAt < 0 then
          // not a single answer since this query was sent: the engine is stuck, start a new one
          stop(l, "KataGo stopped answering")
        else
          // just slow (a busy server): don't let the search go on for nothing
          Try:
            l.synchronized:
              l.conn.send(
                Json.stringify(Json.obj("id" -> s"stop-$id", "action" -> "terminate", "terminateId" -> id))
              )
        promise.tryFailure(TimeoutException(s"KataGo did not answer within $timeout"))

    def close(): Unit = synchronized(live).foreach(stop(_, "shutting down"))

  object Engine:
    private val timer = Executors.newSingleThreadScheduledExecutor: r =>
      val t = Thread(r, "katago-timeout")
      t.setDaemon(true)
      t

  private def daemon(name: String)(body: => Unit): Thread =
    val t = Thread(() => body, name)
    t.setDaemon(true)
    t.start()
    t

  /* ---------------------------------------------------------------------------------- */
  /* Queries                                                                            */

  /** The server's rules: area scoring, positional superko, no suicide, nothing extra for
    * handicap stones (see goban.Scoring).
    */
  val rules = Json.obj(
    "ko" -> "POSITIONAL",
    "scoring" -> "AREA",
    "tax" -> "NONE",
    "suicide" -> false,
    "hasButton" -> false,
    "whiteHandicapBonus" -> "0"
  )

  def colorName(c: chess.Color) = if c.black then "B" else "W"

  def vertex(m: Move, size: Int): String = m match
    case Move.Pass => "pass"
    case Move.Play(p) => p.human(size)

  def parseVertex(str: String, size: Int): Option[Move] =
    if str.equalsIgnoreCase("pass") then Some(Move.Pass) else Point.fromHuman(str, size).map(Move.Play(_))

  /** The moves with their colors. A pass followed by a pass changes nothing on the board, but
    * could end the game for KataGo: such pairs are left out. They happen when play resumes
    * because the players disagreed about the dead stones, and at the end of the game.
    */
  def history(game: Game): List[(chess.Color, Move)] =
    val first = Game.colorToMoveAt(game.startedAtPly)
    game.moves.toList.zipWithIndex
      .map((m, i) => (if i % 2 == 0 then first else !first) -> m)
      .foldLeft(List.empty[(chess.Color, Move)]):
        case ((_, Move.Pass) :: earlier, (_, Move.Pass)) => earlier
        case (acc, cm) => cm :: acc
      .reverse

  /** A query for `game`'s current position. KataGo accepts komi in half points only. */
  def query(
      game: Game,
      visits: Int,
      profile: Option[String] = None,
      policy: Boolean = false,
      ownership: Boolean = false
  ): JsObject =
    val n = game.board.width
    Json.obj(
      "rules" -> rules,
      "komi" -> math.round(game.komi.value * 2) / 2d,
      "boardXSize" -> n,
      "boardYSize" -> n,
      "initialStones" -> Handicap.points(game.size, game.handicap).map(p => Json.arr("B", p.human(n))),
      "initialPlayer" -> colorName(Game.colorToMoveAt(game.startedAtPly)),
      "moves" -> history(game).map((c, m) => Json.arr(colorName(c), vertex(m, n))),
      "maxVisits" -> visits.max(1),
      "analysisPVLen" -> 1,
      "includePolicy" -> policy,
      "includeOwnership" -> ownership
    ) ++ profile.fold(Json.obj())(p => Json.obj("overrideSettings" -> Json.obj("humanSLProfile" -> p)))

  /* ---------------------------------------------------------------------------------- */
  /* Answers                                                                            */

  case class MoveInfo(move: Move, visits: Int, order: Int)

  /** KataGo's answer. Winrate and score are for Black (`reportAnalysisWinratesAs = BLACK` in
    * conf/katago.cfg); the policies and ownership are indexed like [[goban.Point]], the
    * policies with one more entry at the end for passing (-1 for illegal moves).
    */
  case class Analysis(
      moves: List[MoveInfo],
      policy: Option[Vector[Double]],
      humanPolicy: Option[Vector[Double]],
      ownership: Option[Vector[Double]],
      blackWinrate: Option[Double],
      blackLead: Option[Double]
  )

  def parse(o: JsObject, size: Int): Analysis =
    def numbers(key: String) = (o \ key).asOpt[List[Double]].map(_.toVector)
    Analysis(
      moves = (o \ "moveInfos")
        .asOpt[List[JsObject]]
        .getOrElse(Nil)
        .flatMap: m =>
          for
            move <- (m \ "move").asOpt[String].flatMap(parseVertex(_, size))
            visits <- (m \ "visits").asOpt[Int]
          yield MoveInfo(move, visits, (m \ "order").asOpt[Int].getOrElse(Int.MaxValue)),
      policy = numbers("policy"),
      humanPolicy = numbers("humanPolicy"),
      ownership = numbers("ownership"),
      blackWinrate = (o \ "rootInfo" \ "winrate").asOpt[Double],
      blackLead = (o \ "rootInfo" \ "scoreLead").asOpt[Double]
    )

  /** Some variety in the opening, less later: KataGo's own defaults for choosing a move. */
  def temperature(turn: Int): Double = 0.70 + (0.85 - 0.70) * math.pow(0.5, turn / 80d)

  /** Pick one, with a chance growing with weight ^ (1 / temperature). */
  def sample[A](weighted: List[(A, Double)], temperature: Double, rng: scala.util.Random): Option[A] =
    val scaled = weighted.collect { case (a, w) if w > 0 => a -> math.pow(w, 1 / temperature) }
    val total = scaled.map(_._2).sum
    if scaled.isEmpty || !(total > 0) then None
    else
      var r = rng.nextDouble() * total
      scaled
        .find { (_, w) =>
          r -= w
          r < 0
        }
        .orElse(scaled.lastOption)
        .map(_._1)

  /** The move to play, or resigning.
    *
    * At a human rank, the move is drawn from the human-style network's policy for that rank,
    * like KataGo's own human-like play; it passes only when KataGo itself would. At full
    * strength, it is drawn among the moves KataGo's search liked best.
    */
  def choose(
      game: Game,
      level: AiEngine.Level,
      a: Analysis,
      resign: Boolean,
      rng: scala.util.Random
  ): AiEngine.Choice =
    val n = game.board.width
    def legal(m: Move) = game.position.play(m).isRight
    def moves(policy: Vector[Double]): List[(Move, Double)] =
      policy.toList.zipWithIndex.collect:
        case (p, i) if p > 0 && i <= n * n =>
          (if i == n * n then Move.Pass else Move.Play(Point.fromIndex(i))) -> p
    // what KataGo would play: the best move of its search, or of its network after one visit
    val best: Option[Move] = a.moves
      .sortBy(_.order)
      .headOption
      .map(_.move)
      .orElse(a.policy.flatMap(p => moves(p).filter((m, _) => legal(m)).maxByOption(_._2).map(_._1)))
    val t = temperature(game.moves.size)
    if resign && hopeless(game, a) then AiEngine.Choice.Resign
    else
      val move = level.profile match
        case Some(_) =>
          if best.contains(Move.Pass) then Some(Move.Pass)
          else
            val human = a.humanPolicy.orElse(a.policy).getOrElse(Vector.empty)
            sample(moves(human).filter((m, _) => !m.isPass && legal(m)), t, rng)
        case None =>
          val most = a.moves.map(_.visits).maxOption.getOrElse(0)
          // moves with a tenth of the best one's visits or more: not the blunders
          val good = a.moves.filter(m => m.visits * 10 >= most && legal(m.move))
          sample(good.map(m => m.move -> m.visits.toDouble), t, rng).orElse(best.filter(legal))
      AiEngine.Choice.Play(move.getOrElse(Move.Pass))

  /** KataGo's resignation settings, as in GTP: almost no chance to win, 40 points behind,
    * and far enough into the game.
    */
  def hopeless(game: Game, a: Analysis): Boolean =
    (for
      winrate <- a.blackWinrate
      lead <- a.blackLead
    yield
      val (mine, ahead) = if game.player.black then (winrate, lead) else (1 - winrate, -lead)
      mine < 0.005 && ahead < -40 && game.moves.size >= 0.4 * game.board.points
    ).getOrElse(false)

  /** Groups that KataGo expects the opponent to own: dead. */
  def deadStones(game: Game, ownership: Vector[Double]): Set[Point] =
    val board = game.board
    if ownership.size != board.points then Set.empty
    else
      val stones = board.allPoints.filterNot(board.isEmpty).toList
      // ownership of each stone's point, for the stone's own color
      def own(p: Point, sign: Double) =
        sign * ownership(p.index) * (if board(p) == Stone.black then 1 else -1)
      // positive ownership should mean Black: check it on the stones, most of which live
      val sign = if stones.map(own(_, 1)).sum < 0 then -1d else 1d
      stones
        .map(board.group(_).stones)
        .distinct
        .filter(g => g.toList.map(own(_, sign)).sum / g.size < -0.5)
        .flatten
        .toSet
