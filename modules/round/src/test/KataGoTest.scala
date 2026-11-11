package lila.round

import play.api.libs.json.*

import java.util.concurrent.{ Executors, LinkedBlockingQueue, TimeUnit, TimeoutException }
import scala.concurrent.Await

import goban.{ Board, BoardSize, Game, Komi, Move, Point, Position }

class KataGoTest extends munit.FunSuite:

  val nine = BoardSize.Nine
  def pt(s: String, size: Int = 9) = Point.fromHuman(s, size).get
  def await[A](f: Fu[A]) = Await.result(f, 10.seconds)

  /** An engine that answers each query with `answer` (None: never), after `delay`. */
  class FakeKataGo(answer: JsObject => Option[JsObject], delay: JsObject => FiniteDuration = _ => 0.seconds):
    var connections = 0
    var queries = List.empty[JsObject]
    private val timer = Executors.newSingleThreadScheduledExecutor()
    def connect(): KataGo.Connection =
      connections += 1
      val out = LinkedBlockingQueue[Option[String]]()
      new KataGo.Connection:
        def send(line: String): Unit =
          val q = Json.parse(line).as[JsObject]
          synchronized { queries = q :: queries }
          answer(q).foreach: a =>
            val reply: Runnable = () => out.put(Some(Json.stringify(a + ("id" -> (q \ "id").get))))
            timer.schedule(reply, delay(q).toMillis, TimeUnit.MILLISECONDS)
        def readLine(): Option[String] = out.take()
        def close(): Unit = out.put(None)

  def tag(q: JsObject) = (q \ "tag").asOpt[String].getOrElse("")

  test("answers are matched to their queries, whatever their order"):
    val fake = FakeKataGo(q => Some(Json.obj("tag" -> tag(q))), q => if tag(q) == "slow" then 300.millis else 0.seconds)
    val engine = KataGo.Engine(() => fake.connect(), 5.seconds)
    val slow = engine.query(Json.obj("tag" -> "slow"))
    val fast = engine.query(Json.obj("tag" -> "fast"))
    assertEquals(tag(await(fast)), "fast")
    assertEquals(tag(await(slow)), "slow")
    assertEquals(fake.connections, 1)
    engine.close()

  test("an error fails its query only"):
    val fake = FakeKataGo(q => Some(if tag(q) == "bad" then Json.obj("error" -> "bad query") else Json.obj()))
    val engine = KataGo.Engine(() => fake.connect(), 5.seconds)
    intercept[KataGo.Error](await(engine.query(Json.obj("tag" -> "bad"))))
    await(engine.query(Json.obj("tag" -> "good")))
    assertEquals(fake.connections, 1)
    engine.close()

  test("a stuck engine times out, and the next query starts a fresh one"):
    val fake = FakeKataGo(q => Option.when(tag(q) != "stuck")(Json.obj()))
    val engine = KataGo.Engine(() => fake.connect(), 300.millis)
    intercept[TimeoutException](await(engine.query(Json.obj("tag" -> "stuck"))))
    await(engine.query(Json.obj("tag" -> "ok")))
    assertEquals(fake.connections, 2)
    engine.close()

  test("a missing engine fails the query"):
    val engine = KataGo.Engine(() => KataGo.Connection.process(List("/nonexistent/katago"), None), 5.seconds)
    intercept[KataGo.Error](await(engine.query(Json.obj())))
    engine.close()

  test("queries describe the whole position, handicap stones included"):
    val g = Game.replay(BoardSize.Nineteen, Komi(0.5), 2, List(Move.parse("dd", 19).get, Move.Pass)).toOption.get
    val q = KataGo.query(g, 8, Some("rank_10k"))
    assertEquals((q \ "initialStones").as[List[List[String]]], List(List("B", "Q16"), List("B", "D4")))
    assertEquals((q \ "initialPlayer").as[String], "W")
    assertEquals((q \ "moves").as[List[List[String]]], List(List("W", "D16"), List("B", "pass")))
    assertEquals((q \ "boardXSize").as[Int], 19)
    assertEquals((q \ "komi").as[Double], 0.5)
    assertEquals((q \ "maxVisits").as[Int], 8)
    assertEquals((q \ "overrideSettings" \ "humanSLProfile").as[String], "rank_10k")
    assertEquals((q \ "rules" \ "ko").as[String], "POSITIONAL")
    assert((KataGo.query(g, 8) \ "overrideSettings").isEmpty)

  test("two passes in a row are left out, so KataGo doesn't see a finished game"):
    val g = Game.replay(nine, Komi(7.5), 0, Game.parseMoves("ee pass pass cc pass pass", nine)).toOption.get
    // SGF rows count from the top: "cc" on 9x9 is C7
    assertEquals(KataGo.history(g), List(chess.Color.Black -> Move.Play(pt("E5")), chess.Color.White -> Move.Play(pt("C7"))))
    // komi in half points only
    assertEquals((KataGo.query(g.copy(komi = Komi(6.4)), 1) \ "komi").as[Double], 6.5)

  // white to play, after black E5
  val afterE5 = Game.replay(nine, Komi(7.5), 0, List(Move.Play(pt("E5")))).toOption.get

  /** A policy with these weights, the rest zero; "pass" is the last entry. */
  def policy(weights: (String, Double)*) =
    val v = Array.fill(82)(0d)
    weights.foreach: (m, w) =>
      v(if m == "pass" then 81 else pt(m).index) = w
    v.toVector

  def analysis(
      moves: List[KataGo.MoveInfo] = Nil,
      policy: Vector[Double] = Vector.empty,
      human: Vector[Double] = Vector.empty
  ) = KataGo.Analysis(moves, Some(policy).filter(_.nonEmpty), Some(human).filter(_.nonEmpty), None, Some(0.5), Some(0d))

  val human = AiEngine.Level(Some("rank_10k"), 1)
  val full = AiEngine.Level(None, 32)
  def choose(level: AiEngine.Level, a: KataGo.Analysis, game: Game = afterE5, seed: Long = 1L) =
    KataGo.choose(game, level, a, resign = true, scala.util.Random(seed))

  test("a human level plays a legal move from the human-style policy"):
    // the occupied E5 must never be played, even with most of the weight
    val a = analysis(policy = policy("C3" -> 1.0), human = policy("E5" -> 0.9, "C3" -> 0.1))
    (1L to 20L).foreach: seed =>
      assertEquals(choose(human, a, seed = seed), AiEngine.Choice.Play(Move.Play(pt("C3"))))

  test("a human level passes only when KataGo would"):
    val endgame = analysis(policy = policy("pass" -> 0.9, "C3" -> 0.1), human = policy("C3" -> 1.0))
    assertEquals(choose(human, endgame), AiEngine.Choice.Play(Move.Pass))
    val playing = analysis(policy = policy("C3" -> 0.9, "pass" -> 0.1), human = policy("pass" -> 0.9, "G7" -> 0.1))
    assertEquals(choose(human, playing), AiEngine.Choice.Play(Move.Play(pt("G7"))))

  test("full strength picks among the moves the search liked, never a blunder"):
    val a = analysis(moves = List(KataGo.MoveInfo(Move.Play(pt("C3")), 30, 0), KataGo.MoveInfo(Move.Play(pt("G7")), 2, 1)))
    (1L to 20L).foreach: seed =>
      assertEquals(choose(full, a, seed = seed), AiEngine.Choice.Play(Move.Play(pt("C3"))))

  test("hopeless games are resigned, but not too early"):
    val lost = analysis().copy(blackWinrate = Some(0.001), blackLead = Some(-60d))
    val long = Game.replay(nine, Komi(7.5), 0, List.fill(34)(Move.Pass)).toOption.get
    assertEquals(choose(full, lost, long), AiEngine.Choice.Resign)
    // white to move: the same numbers are winning
    assertNotEquals(choose(full, lost, long.play(Move.Pass).toOption.get), AiEngine.Choice.Resign)
    assertNotEquals(choose(full, lost, afterE5.play(Move.Play(pt("C3"))).toOption.get), AiEngine.Choice.Resign)

  test("answers are parsed"):
    val json = Json.parse(
      """{"id":"1","isDuringSearch":false,"turnNumber":1,
        |"moveInfos":[{"move":"C3","visits":12,"order":0,"winrate":0.4},{"move":"pass","visits":1,"order":1}],
        |"rootInfo":{"winrate":0.45,"scoreLead":-1.5,"currentPlayer":"W"},
        |"ownership":[0.5,-0.5]}""".stripMargin
    )
    val a = KataGo.parse(json.as[JsObject], 9)
    assertEquals(a.moves, List(KataGo.MoveInfo(Move.Play(pt("C3")), 12, 0), KataGo.MoveInfo(Move.Pass, 1, 1)))
    assertEquals(a.blackWinrate, Some(0.45))
    assertEquals(a.blackLead, Some(-1.5))
    assertEquals(a.ownership, Some(Vector(0.5, -0.5)))
    assertEquals(a.policy, None)

  test("dead stones: groups in the opponent's area"):
    // black's area on the left, white's on the right, a lone black stone at G5 in white's
    val rows = Seq("...bw....", "...bw....", "...bw....", "...bw....", "...bw.b..", "...bw....", "...bw....",
      "...bw....", "...bw....")
    val board = Board.parse(nine, rows.mkString).get
    val g = Game(nine, Komi(7.5), 0, None).copy(position = Position(board, chess.Color.Black, consecutivePasses = 2))
    val ownership = (0 until 81).map(i => if i % 9 <= 3 then 0.9 else -0.9).toVector
    assertEquals(KataGo.deadStones(g, ownership), Set(pt("G5")))
    // the same from White's point of view
    assertEquals(KataGo.deadStones(g, ownership.map(-_)), Set(pt("G5")))
    assertEquals(KataGo.deadStones(g, Vector(1d)), Set.empty[Point])

  // Runs against a real engine when KATAGO_COMMAND is set, e.g.
  // KATAGO_COMMAND="$PWD/katago/katago analysis -model $PWD/katago/model.bin.gz \
  //   -human-model $PWD/katago/human.bin.gz -config $PWD/conf/katago.cfg" (see README)
  test("real engine: legal moves for many games at once, and dead stones"):
    assume(sys.env.contains("KATAGO_COMMAND"), "KATAGO_COMMAND is not set")
    given scala.concurrent.ExecutionContextExecutor = scala.concurrent.ExecutionContext.global
    val command = sys.env("KATAGO_COMMAND").split(' ').toList.filter(_.nonEmpty)
    val ai = AiEngine(
      AiEngine.Config(command, None, List(AiEngine.Level(Some("rank_10k"), 1), AiEngine.Level(None, 8)), 2.minutes, true, 16)
    )
    val g = Game.replay(nine, Komi(7.5), 0, Game.parseMoves("ee cc", nine)).toOption.get
    // several games asking at the same time, at both kinds of levels
    val choices = Await.result(Future.sequence((1 to 8).map(i => ai.move(g, 1 + i % 2))), 2.minutes)
    choices.foreach:
      case AiEngine.Choice.Play(m) => assert(g.position.play(m).isRight, s"illegal move $m")
      case AiEngine.Choice.Resign => fail("resigned in the opening")
    // a lone white stone inside black's area is dead
    val rows = Seq("....bw...", "....bw...", ".w..bw...", "....bw...", "....bw...", "....bw...", "....bw...",
      "....bw...", "....bw...")
    val board = Board.parse(nine, rows.mkString).get
    val ended = Game(nine, Komi(7.5), 0, None).copy(position = Position(board, chess.Color.Black))
    // positions are sent as moves: feed the stones as moves, passing when one color runs out
    val setup = board.allPoints.toList.filterNot(board.isEmpty)
    val (blacks, whites) = setup.partition(board(_) == goban.Stone.black)
    val moves = blacks.map(Some(_)).zipAll(whites.map(Some(_)), None, None).flatMap: (b, w) =>
      List(b.fold(Move.Pass)(Move.Play(_)), w.fold(Move.Pass)(Move.Play(_)))
    val game = Game.replay(nine, Komi(7.5), 0, moves :+ Move.Pass :+ Move.Pass).toOption.get
    assertEquals(game.board, ended.board)
    val dead = Await.result(ai.deadStones(game), 2.minutes)
    assert(dead.contains(pt("B7")), s"dead: $dead")
    assert(dead.forall(_ == pt("B7")), s"dead: $dead")
    ai.close()

  /** The default levels (conf/base.conf), played by the engine in KATAGO_COMMAND. Resigning
    * is off, so that games go on until both players pass.
    */
  def defaultLevels(using Executor) =
    val conf = play.api.Configuration(com.typesafe.config.ConfigFactory.parseFile(java.io.File("conf/base.conf")))
    val command = sys.env("KATAGO_COMMAND").split(' ').toList.filter(_.nonEmpty)
    AiEngine(AiEngine.config(conf).copy(command = command, workDir = None, timeout = 2.minutes, resign = false))

  /** The computer plays both colors at `level`, until both pass or `maxMoves`. */
  def selfPlay(ai: AiEngine, start: Game, level: Int, maxMoves: Int): Game =
    val n = start.board.width
    var g = start
    while !g.bothPassed && g.moves.size < maxMoves do
      Await.result(ai.move(g, level), 2.minutes) match
        case AiEngine.Choice.Play(m) =>
          g = g.play(m).fold(e => fail(s"illegal move ${m.human(n)} after ${g.moveKeys}: $e"), identity)
        case AiEngine.Choice.Resign => fail("resigned, with resigning off")
    g

  for level <- List(1, 8) do
    test(s"real engine: a complete 9x9 game at level $level ends with passes and sensible dead stones"):
      assume(sys.env.contains("KATAGO_COMMAND"), "KATAGO_COMMAND is not set")
      given scala.concurrent.ExecutionContextExecutor = scala.concurrent.ExecutionContext.global
      val ai = defaultLevels
      val g = selfPlay(ai, Game(nine, Komi(7.5), 0, None), level, lila.game.Game.maxPlies.value)
      val board = g.board
      val dead = Await.result(ai.deadStones(g), 2.minutes)
      val score = goban.Scoring.area(board, dead, g.komi)
      println(s"level $level: ${g.moves.size} moves, ${score.result}, dead ${dead.map(_.human(9)).toList.sorted}\n$board")
      // over by passing, long before the server's move limit, and not by passing early
      assert(g.bothPassed, s"no end after ${g.moves.size} moves")
      assert(board.stoneCount(chess.Color.Black) + board.stoneCount(chess.Color.White) >= 20, board.toString)
      // dead stones: whole groups, each in its opponent's area once the dead are removed
      dead.foreach: p =>
        assert(!board.isEmpty(p))
        assert(board.group(p).stones.subsetOf(dead), s"${p.human(9)}: part of a group")
        assertEquals(score.owner.get(p), board.colorAt(p).map(!_), s"${p.human(9)} is not in its opponent's area")
      ai.close()

  test("real engine: a 19x19 handicap game starts with white, at a human level and at full strength"):
    assume(sys.env.contains("KATAGO_COMMAND"), "KATAGO_COMMAND is not set")
    given scala.concurrent.ExecutionContextExecutor = scala.concurrent.ExecutionContext.global
    val ai = defaultLevels
    val start = Game(BoardSize.Nineteen, Komi(0.5), 4, None)
    assertEquals(start.player, chess.Color.White)
    for level <- List(3, 8) do
      val g = selfPlay(ai, start, level, 6)
      assertEquals(g.moves.size, 6)
      assert(!g.moves.exists(_.isPass), g.moveKeys)
      // the four handicap stones and the six moves
      assertEquals(g.board.stoneCount(chess.Color.Black) + g.board.stoneCount(chess.Color.White), 10)
    ai.close()
