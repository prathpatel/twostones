package lila.ws

import cats.syntax.option.*
import chess.Color
import org.apache.pekko.actor.typed.ActorRef

import lila.ws.ipc.*

/* Manages subscriptions to position updates, for the mini boards of live games */
object MiniGames:

  case class Watched(position: Option[Position], clients: Set[ActorRef[ClientMsg]])

  private val games = scalalib.ConcurrentMap[Game.Id, Watched](1024)
  export games.size

  // client starts watching
  def watch(gameIds: Iterable[Game.Id], client: Client): Unit =
    gameIds.foreach: gameId =>
      games
        .compute(gameId):
          case None => Watched(None, Set(client)).some
          case Some(Watched(square, clients)) => Watched(square, clients + client).some
        .flatMap(_.position)
        .foreach: p =>
          client ! ClientIn.MiniGamePosition(gameId, p)

  // when a client disconnects
  def unwatch(gameIds: Iterable[Game.Id], client: Client): Unit =
    gameIds.foreach: gameId =>
      games.computeIfPresent(gameId): watched =>
        val newClients = watched.clients - client
        Option.when(newClients.nonEmpty)(watched.copy(clients = newClients))

  // a game finishes
  def finish(gameId: Game.Id, winner: Option[Color]) =
    games.computeIfPresent(gameId): watched =>
      watched.clients.foreach { _ ! ClientIn.Finish(gameId, winner) }
      none

  // move coming from the server
  def move(gameId: Game.Id, json: JsonString, moveBy: Option[Color]): Unit =
    games.computeIfPresent(gameId): watched =>
      val turnColor = moveBy.fold(Color.white)(c => !c)
      json.value
        .match
          case MoveClockRegex(moveS, boardS, wcS, bcS) =>
            for
              wc <- wcS.toIntOption
              bc <- bcS.toIntOption
            yield Position(moveS, boardS, Some(Clock(wc, bc)), turnColor)
          case MoveRegex(moveS, boardS) => Some(Position(moveS, boardS, None, turnColor))
          case _ => None
        .fold(watched): position =>
          val msg = ClientIn.MiniGamePosition(gameId, position)
          watched.clients.foreach { _ ! msg }
          watched.copy(position = Some(position))
        .some

  // {"move":"pd","coord":"Q16","board":"....b....w...",...,"clock":{"white":121.88,"black":120.94}
  private val MoveRegex = """move":"([^"]+)".+board":"([^"]+)""".r.unanchored
  private val MoveClockRegex = """move":"([^"]+)".+board":"([^"]+).+white":(\d+).+black":(\d+)""".r.unanchored
