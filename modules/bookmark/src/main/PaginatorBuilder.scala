package lila.bookmark

import scalalib.paginator.*

import lila.core.game.{ Game, GameRepo }
import lila.db.dsl.{ *, given }

final class PaginatorBuilder(
    coll: Coll,
    gameRepo: GameRepo
)(using Executor):

  def byUser(user: User, page: Int): Fu[Paginator[Game]] =
    Paginator(
      new UserAdapter(user),
      currentPage = page,
      maxPerPage = MaxPerPage(12)
    )

  /** The games bookmarked by [[userId]], the most recently created first, created before [[until]]. */
  def gamesByUser(userId: UserId, max: Int, until: Option[Instant]): Fu[List[Game]] =
    coll
      .aggregateList(max, _.sec): framework =>
        import framework.*
        Match(bdoc("u" -> userId)) -> List(
          Project(bdoc("_id" -> false, "g" -> true)),
          PipelineOperator(
            bdoc(
              "$lookup" -> bdoc(
                "from" -> gameRepo.coll.name,
                "as" -> "game",
                "localField" -> "g",
                "foreignField" -> "_id"
              )
            )
          ),
          Unwind("game"),
          ReplaceRootField("game"),
          Match(until.so(d => bdoc("ca" -> bdoc("$lt" -> d)))),
          Sort(Descending("ca")),
          Limit(max)
        )
      .map(_.flatMap(gameRepo.gameHandler.readOpt))

  final class UserAdapter(user: User) extends AdapterLike[Game]:

    def nbResults: Fu[Int] = coll.countSel(selector)

    def slice(offset: Int, length: Int): Fu[Seq[Game]] =
      coll
        .aggregateList(length, _.sec): framework =>
          import framework.*
          Match(selector) -> List(
            Sort(Descending("d")),
            Skip(offset),
            Limit(length),
            Project(bdoc("_id" -> false, "g" -> true)),
            PipelineOperator(
              bdoc(
                "$lookup" -> bdoc(
                  "from" -> gameRepo.coll.name,
                  "as" -> "game",
                  "localField" -> "g",
                  "foreignField" -> "_id"
                )
              )
            ),
            Unwind("game"),
            ReplaceRootField("game")
          )
        .map:
          _.flatMap(gameRepo.gameHandler.readOpt)

    private def selector = bdoc("u" -> user.id)
