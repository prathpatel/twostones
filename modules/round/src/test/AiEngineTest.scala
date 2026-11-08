package lila.round

import scala.concurrent.Await

import play.api.Configuration

class AiEngineTest extends munit.FunSuite:

  given scala.concurrent.ExecutionContextExecutor = scala.concurrent.ExecutionContext.global

  val game = goban.Game(goban.BoardSize.Nine, goban.Komi(7.5), 0, None)

  import AiEngine.{ Config, Level }

  val humanLevels = List(Level(Some("rank_20k"), 1), Level(Some("rank_5k"), 1), Level(None, 64))

  def config(command: String) = Config(command.split(' ').toList, None, humanLevels, 2.seconds, true, 16)

  test("levels past the list use the last one"):
    val c = config("katago")
    assertEquals(c.levelOf(1), Level(Some("rank_20k"), 1))
    assertEquals(c.levelOf(8), Level(None, 64))
    assertEquals(c.levelOf(2).rank, Some("5k"))

  test("the computer's name shows the rank it imitates"):
    val ai = AiEngine(config("/nonexistent/katago analysis"))
    assertEquals(ai.engineFor(1), Some("KataGo 20k"))
    assertEquals(ai.engineFor(8), Some("KataGo"))
    ai.close()

  test("without KataGo there is no move, and no dead stones proposed"):
    val ai = AiEngine(config("/nonexistent/katago analysis"))
    intercept[KataGo.Error](Await.result(ai.move(game, 5), 10.seconds))
    assertEquals(Await.result(ai.deadStones(game), 10.seconds), Set.empty[goban.Point])
    ai.close()

  test("the default config: KataGo's analysis engine plays every level, 20 kyu to full strength"):
    val conf = Configuration(com.typesafe.config.ConfigFactory.parseFile(java.io.File("conf/base.conf")))
    val c = AiEngine.config(conf)
    assertEquals(c.levels.size, 8)
    assertEquals(c.levelOf(1).rank, Some("20k"))
    assertEquals(c.levelOf(8).profile, None)
    assert(c.deadStonesVisits > 0)
    // existing files in the command are made absolute, the rest is left alone
    assert(c.command.contains("analysis"))
    assert(c.command.exists(a => a.startsWith("/") && a.endsWith("conf/katago.cfg")))
    assert(c.workDir.exists(_.isAbsolute))
