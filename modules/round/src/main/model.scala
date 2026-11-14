package lila.round

import alleycats.Zero

opaque type ScheduleExpiration = Game => Unit
object ScheduleExpiration extends FunctionWrapper[ScheduleExpiration, Game => Unit]

opaque type UrgentGames = List[Pov]
object UrgentGames extends TotalWrapper[UrgentGames, List[Pov]]:
  given Zero[UrgentGames] = Zero(UrgentGames(Nil))
  extension (u: UrgentGames)
    def selectNext(from: Game): Option[Pov] =
      u.value.find: pov =>
        pov.isMyTurn &&
          (pov.game.hasClock || !from.hasClock) &&
          (pov.game.nonAi || from.hasAi)

/** Asks for the JSON of a game, as the mobile app receives it when its game socket connects.
  *
  * [[pov]] is the side of the player when [[asPlayer]] (the request carried the player's full ID),
  * otherwise the side a spectator watches from.
  */
case class MobileGameJson(
    pov: Pov,
    asPlayer: Boolean,
    me: Option[MyId],
    promise: Promise[play.api.libs.json.JsObject]
)
