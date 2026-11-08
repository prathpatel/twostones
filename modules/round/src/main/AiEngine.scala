package lila.round

import play.api.Configuration
import play.api.libs.json.Json

/** Chooses the computer's moves and estimates dead stones, with KataGo's analysis engine
  * ([[KataGo]]): one process for all games, which searches many positions in parallel.
  *
  * Levels 1-7 imitate human players from 20 kyu to 9 dan with KataGo's human-style network,
  * level 8 plays at full strength. There is no other engine: while KataGo is missing or
  * failing, the computer doesn't move, and the round asks again (see MovePlayer.requestAi).
  */
final class AiEngine(config: AiEngine.Config)(using Executor):

  import AiEngine.*

  private val katago = KataGo.Engine(() => KataGo.Connection.process(config.command, config.workDir), config.timeout)

  // start the engine now: loading the networks takes a few seconds
  katago
    .query(Json.obj("action" -> "query_version"))
    .onComplete:
      case scala.util.Success(v) =>
        logger.info(s"AI engine ready: KataGo ${(v \ "version").asOpt[String].getOrElse("")}")
      case scala.util.Failure(e) =>
        logger.warn(
          s"AI engine failed to start (${e.getMessage}). Run bin/setup-katago to install KataGo: " +
            "until then the computer can't play."
        )

  /** How long to wait for an AI move before asking again: longer than the engine timeout. */
  def watchdogDelay: FiniteDuration = config.timeout + 15.seconds

  /** The computer's name for this level, for display: "KataGo 10k". */
  def engineFor(level: Int): Option[String] =
    Some(config.levelOf(level).rank.fold("KataGo")(r => s"KataGo $r"))

  /** Fails when KataGo is missing, stopped or too slow. */
  def move(game: goban.Game, level: Int): Fu[Choice] =
    val l = config.levelOf(level)
    katago
      .query(KataGo.query(game, l.visits, l.profile, policy = true))
      .map: answer =>
        KataGo.choose(game, l, KataGo.parse(answer, game.board.width), config.resign, scala.util.Random)

  /** Dead stones once both players passed, as a starting point for counting: none when KataGo
    * fails, and the players mark them.
    */
  def deadStones(game: goban.Game): Fu[Set[goban.Point]] =
    katago
      .query(KataGo.query(game, config.deadStonesVisits, ownership = true))
      .map(KataGo.parse(_, game.board.width).ownership.fold(Set.empty[goban.Point])(KataGo.deadStones(game, _)))
      .recover: e =>
        logger.warn(s"AI engine dead stones failed (${e.getMessage})")
        Set.empty[goban.Point]

  def close(): Unit = katago.close()

object AiEngine:

  enum Choice:
    case Play(move: goban.Move)
    case Resign

  /** One computer level: a human rank for KataGo's human-style network to imitate
    * ("rank_20k" ... "rank_9d"), or none for full strength, and the search size.
    */
  case class Level(profile: Option[String], visits: Int):
    /** "rank_10k" => "10k" */
    def rank: Option[String] = profile.map(_.split('_').last)

  case class Config(
      /** the engine command; arguments that name existing files are made absolute */
      command: List[String],
      /** where the engine runs and writes its logs */
      workDir: Option[java.io.File],
      /** levels 1, 2, ... 8 */
      levels: List[Level],
      timeout: FiniteDuration,
      resign: Boolean,
      deadStonesVisits: Int
  ):
    def levelOf(level: Int): Level =
      levels.lift(level - 1).orElse(levels.lastOption).getOrElse(Level(None, 32))

  /** Relative paths in the config are relative to the directory lila runs in. */
  private def absolute(path: String) = java.io.File(path).getAbsoluteFile

  def config(app: Configuration): Config =
    val c = app.get[Configuration]("round.ai.katago")
    Config(
      command = c
        .get[String]("command")
        .split(' ')
        .toList
        .filter(_.nonEmpty)
        .map: arg =>
          if !arg.startsWith("-") && java.io.File(arg).exists then absolute(arg).getPath else arg,
      workDir = c.getOptional[String]("work_dir").filter(_.nonEmpty).map(absolute),
      levels = c
        .getOptional[Seq[Configuration]]("levels")
        .map(_.toList.map: l =>
          Level(l.getOptional[String]("profile").filter(_.nonEmpty), l.getOptional[Int]("visits") | 1))
        .getOrElse(Nil),
      timeout = c.getOptional[FiniteDuration]("timeout").getOrElse(30.seconds),
      resign = c.getOptional[Boolean]("resign").getOrElse(true),
      deadStonesVisits = c.getOptional[Int]("dead_stones_visits").getOrElse(16)
    )
