package controllers

import play.api.libs.json.*
import play.api.mvc.*

import lila.app.{ *, given }
import lila.common.Json.given
import lila.core.LightUser
import lila.db.dsl.{ *, given }
import lila.game.BSONHandlers.gameHandler
import lila.game.Query

/** The games of the TwoStones app: one game to replay, and the lists of the games of a player, of
  * the bookmarks and of a set of IDs, as ND-JSON.
  */
final class MobileGame(env: Env) extends LilaController(env):

  private def maxGames(using RequestHeader) = getInt("max").fold(20)(_.squeeze(1, 100))

  private def until(using RequestHeader): Option[Instant] = getLong("until").map(millisToInstant)

  /** GET /api/mobile/game/:id */
  def game(id: GameId) = OpenOrScoped(_.Web.Mobile): ctx ?=>
    Found(env.round.proxyRepo.gameIfPresentOrFetch(id)): game =>
      for
        lightUsers <- preloadUsers(List(game))
        bookmarked <- getBool("withBookmarked").so(env.bookmark.api.exists(game, ctx.userId).dmap(some))
      yield JsonOk:
        lightJson(game, bookmarked)(using lightUsers) ++ Json
          .obj(
            "moves" -> game.go.moveKeys,
            "clocks" -> game.bothClockStates.map(_.map(_.centis))
          )
          .add("scoring" -> (lila.game.JsonView.goJson(game) \ "scoring").asOpt[JsObject])

  /** GET /api/mobile/games/user/:user */
  def byUser(username: UserStr) = OpenOrScoped(_.Web.Mobile): ctx ?=>
    Found(env.user.repo.enabledById(username)): user =>
      val perfs = get("perfType").so(_.split(',').toSet)
      val color = getColor("color")
      val vs = get("vs").flatMap(UserStr.read).map(_.id)
      val selector = Query.user(user.id) ++
        vs.so(v => bdoc("us" -> bdoc("$all" -> List(user.id, v)))) ++
        until.so(d => bdoc(lila.core.game.BSONFields.createdAt -> bdoc("$lt" -> d)))
      val filtered = perfs.nonEmpty || color.isDefined
      for
        found <- env.game.gameRepo.coll
          .find(selector)
          .sort(Query.sortCreated)
          .cursor[lila.core.game.Game](ReadPref.sec)
          .list(if filtered then maxGames * 5 else maxGames)
        games = found
          .filter: g =>
            (perfs.isEmpty || perfs.contains(g.perfKey.value)) &&
              color.forall(c => g.player(c).isUser(user.id))
          .take(maxGames)
        res <- gamesNdJson(games, withBookmarked = getBool("withBookmarked"))
      yield res

  /** GET /api/mobile/games/bookmarks */
  def bookmarks = Scoped(_.Web.Mobile) { ctx ?=> me ?=>
    for
      games <- env.bookmark.api.gamesByUser(me.userId, maxGames, until)
      lightUsers <- preloadUsers(games)
    yield jsToNdJson(games.map(lightJson(_, bookmarked = true.some)(using lightUsers)))
  }

  /** POST /api/mobile/games/ids, with the comma separated game IDs as the body */
  def byIds = AnonOrScopedBody(parse.tolerantText)(_.Web.Mobile): ctx ?=>
    val ids = ctx.body.body.split(',').view.map(_.trim).flatMap(GameId.from).take(300).toSeq
    env.game.gameRepo.gamesFromSecondary(ids).flatMap(gamesNdJson(_, withBookmarked = false))

  private def gamesNdJson(games: Seq[lila.core.game.Game], withBookmarked: Boolean)(using
      ctx: Context
  ): Fu[Result] =
    for
      lightUsers <- preloadUsers(games)
      bookmarks <-
        if withBookmarked then env.bookmark.api.filterGameIdsBookmarkedBy(games, ctx.me.map(_.value))
        else fuccess(Set.empty[GameId])
    yield jsToNdJson:
      games.map: g =>
        lightJson(g, withBookmarked.option(bookmarks.contains(g.id)))(using lightUsers)

  private def preloadUsers(games: Seq[lila.core.game.Game]): Fu[LightUser.GetterSync] =
    env.user.lightUserApi.preloadMany(games.flatMap(_.userIds).distinct).inject(env.user.lightUserSync)

  private def lightJson(g: lila.core.game.Game, bookmarked: Option[Boolean])(using
      lightUser: LightUser.GetterSync
  ): JsObject =
    def playerJson(color: chess.Color) =
      val p = g.player(color)
      Json
        .obj()
        .add("user" -> p.userId.flatMap(lightUser))
        .add("name" -> p.name.map(_.value))
        .add("rating" -> p.rating.map(_.value))
        .add("ratingDiff" -> p.ratingDiff.map(_.value))
        .add("provisional" -> p.provisional.yes)
        .add("aiLevel" -> p.aiLevel)
    Json
      .obj(
        "id" -> g.id,
        "rated" -> g.rated.yes,
        "speed" -> g.speed.key,
        "perf" -> g.perfKey,
        "size" -> g.boardSize.size,
        "handicap" -> g.handicap,
        "komi" -> g.komi.value,
        "createdAt" -> g.createdAt.toMillis,
        "lastMoveAt" -> g.movedAt.toMillis,
        "status" -> g.status.name,
        "players" -> Json.obj("white" -> playerJson(chess.White), "black" -> playerJson(chess.Black)),
        "board" -> g.board.serialize
      )
      .add("source" -> g.source.map(_.name))
      .add("winner" -> g.winnerColor.map(_.name))
      .add("lastMove" -> g.lastMoveKey)
      .add("result" -> lila.game.SgfDump.result(g))
      .add("clock" -> g.clock.map: c =>
        Json.obj(
          "initial" -> c.limitSeconds.value,
          "increment" -> c.incrementSeconds.value,
          "totalTime" -> c.estimateTotalSeconds
        ))
      .add("daysPerTurn" -> g.daysPerTurn.map(_.value))
      .add("bookmarked" -> bookmarked)
