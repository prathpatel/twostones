package controllers

import play.api.mvc.*

import lila.app.{ *, given }
import lila.core.id.GameAnyId

final class Game(env: Env) extends LilaController(env):

  def bookmark(gameId: GameId) = AuthOrScopedBody(_.Web.Mobile, _.Preference.Write) { _ ?=> me ?=>
    env.bookmark.api
      .toggle(env.round.gameProxy.updateIfPresent)(gameId, me, getBoolOpt("v"))
      .inject(NoContent)
  }

  /** Download a game as SGF. */
  def exportOne(id: GameAnyId) = OpenOrScoped(): ctx ?=>
    Found(env.round.proxyRepo.gameIfPresentOrFetch(id.gameId)): game =>
      env.game.sgfDump(game, withRating = ctx.pref.showRatings).map: sgf =>
        Ok(sgf)
          .asAttachment(env.game.sgfDump.filename(game))
          .withHeaders(headersForApiOrApp*)
          .as("application/x-go-sgf")

  private[controllers] def preloadUsers(game: lila.core.game.Game): Funit =
    env.user.lightUserApi.preloadMany(game.userIds)
  private[controllers] def preloadUsers(users: lila.core.user.GameUsers): Unit =
    env.user.lightUserApi.preloadUsers(users.all.collect:
      case Some(lila.core.user.WithPerf(u, _)) => u)
