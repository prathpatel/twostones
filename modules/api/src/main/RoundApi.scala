package lila.api

import play.api.libs.json.*
import scalalib.data.Preload

import lila.api.Context.given
import lila.common.Json.given
import lila.core.perm.Granter
import lila.core.user.GameUsers
import lila.mon.extensions.*
import lila.round.{ ExportOptions, JsonView }
import lila.round.RoundGame.{ hasChat, justCreated }

final private[api] class RoundApi(
    jsonView: JsonView,
    noteApi: lila.round.NoteApi,
    bookmarkApi: lila.bookmark.BookmarkApi,
    userApi: lila.user.UserApi,
    prefApi: lila.pref.PrefApi,
    userLag: lila.socket.UserLagCache,
    chatApi: lila.chat.ChatApi,
    chatJson: lila.chat.ChatJsonView
)(using Executor):

  /** The game as the mobile app receives it on its game socket, without an HTTP request: the
    * player's view when [[asPlayer]], otherwise a spectator's.
    */
  def mobile(pov: Pov, asPlayer: Boolean, meId: Option[MyId]): Fu[JsObject] =
    for
      me <- meId.so(userApi.me)
      myPref <- me.fold(fuccess(lila.pref.Pref.default))(m => prefApi.get(m.value))
      users <- userApi.gamePlayers(pov.game.userIdPair, pov.game.perfKey)
      flags = ExportOptions(rating = myPref.showRatings)(using me)
      json <-
        if asPlayer then
          prefApi
            .get(users.map(_.map(_.user)), pov.color, myPref)
            .flatMap(jsonView.playerJson(pov, _, users, flags))
            .dmap(withOpponentSignal(pov))
        else jsonView.watcherJson(pov, users, myPref.some, meId.map(_.userId), none, flags)
      chat <- (asPlayer && pov.game.hasChat).so:
        chatApi.playerChat
          .findIf(pov.game.id.into(ChatId), !pov.game.justCreated)
          .flatMap(chatJson.asyncLines)
          .dmap(lines => Json.obj("lines" -> lines.value).some)
      bookmarked <- bookmarkApi.exists(pov.game, meId.map(_.userId))
    yield withBookmark(bookmarked)(json).add("chat" -> chat)

  def player(pov: Pov, users: Preload[GameUsers])(using ctx: Context): Fu[JsObject] = {
    for
      users <- users.orLoad(userApi.gamePlayers(pov.game.userIdPair, pov.game.perfKey))
      prefs <- prefApi.get(users.map(_.map(_.user)), pov.color, ctx.pref)
      json <- jsonView.playerJson(pov, prefs, users, ctxFlags)
      bookmarked <- bookmarkApi.exists(pov.game, ctx.me)
    yield withBookmark(bookmarked).compose(withOpponentSignal(pov))(json)
  }.mon(lila.mon.round.api.player)

  def watcher(
      pov: Pov,
      users: GameUsers,
      tv: Option[lila.round.OnTv]
  )(using ctx: Context): Fu[JsObject] = {
    for
      json <- jsonView.watcherJson(pov, users, ctx.pref.some, ctx.me, tv, ctxFlags)
      bookmarked <- bookmarkApi.exists(pov.game, ctx.me)
    yield withBookmark(bookmarked)(json)
  }.mon(lila.mon.round.api.watcher)

  /** A finished game, for replay. */
  def review(pov: Pov, users: GameUsers)(using ctx: Context): Fu[JsObject] =
    watcher(pov, users, none)

  private def ctxFlags(using ctx: Context) =
    ExportOptions(
      blurs = Granter.opt(_.ViewBlurs),
      rating = ctx.pref.showRatings,
      nvui = ctx.blind
    )

  private def withBookmark(v: Boolean)(json: JsObject) =
    json.add("bookmarked" -> v)

  private def withOpponentSignal(pov: Pov)(json: JsObject) =
    if pov.game.speed <= chess.Speed.Blitz then
      json.add("opponentSignal", pov.opponent.userId.flatMap(userLag.getLagRating))
    else json
