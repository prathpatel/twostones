package lila.ws

import chess.{ ByColor, Color }
import scalalib.SecureRandom

opaque type RoomId = String
object RoomId extends OpaqueString[RoomId]:
  def ofPlayer(id: Game.FullId): RoomId = id.gameId.value

object Game:

  opaque type Id = String
  object Id extends OpaqueString[Id]:
    extension (id: Id) def full(playerId: PlayerId): FullId = FullId(s"$id$playerId")

  opaque type FullId = String
  object FullId extends OpaqueString[FullId]:
    extension (fullId: FullId)
      def gameId = Game.Id(fullId.value.take(8))
      def playerId = PlayerId(fullId.value.drop(8))

  opaque type AnyId = String
  object AnyId extends OpaqueString[AnyId]

  opaque type PlayerId = String
  object PlayerId extends OpaqueString[PlayerId]

  case class Player(id: PlayerId, userId: Option[User.Id])

  // must only contain invariant data (no status, turns, or termination)
  // because it's cached in Mongo.scala
  case class Round(id: Game.Id, players: ByColor[Player]):
    def player(id: PlayerId, userId: Option[User.Id]): Option[RoundPlayer] =
      players.zipColor.collect:
        case (c, p) if p.id == id && p.userId == userId => RoundPlayer(id, c)

  case class RoundPlayer(id: PlayerId, color: Color)
end Game

object Challenge:
  opaque type Id = String
  object Id extends OpaqueString[Id]
  enum Challenger:
    case Anon(secret: String)
    case User(userId: lila.ws.User.Id)
    case Open

opaque type Sri = String
object Sri extends OpaqueString[Sri]:
  def random() = Sri(SecureRandom.nextString(12))
  def from(str: String): Option[Sri] = Option.unless(str.contains(' '))(str)

opaque type Flag = String
object Flag extends OpaqueString[Flag]:
  def make(value: String) =
    value match
      case "api" | "embed" => Some(Flag(value))
      case _ => None
  val api = Flag("api")
  val embed = Flag("embed")

opaque type IpAddress = String
object IpAddress extends OpaqueString[IpAddress]

opaque type JsonString = String
object JsonString extends OpaqueString[JsonString]

opaque type SocketVersion = Int
object SocketVersion extends OpaqueInt[SocketVersion]

opaque type IsTroll = Boolean
object IsTroll extends YesNo[IsTroll]

case class UptimeMillis(millis: Long) extends AnyVal:
  def toNow: Long = UptimeMillis.make.millis - millis

object UptimeMillis:
  def make = UptimeMillis(System.currentTimeMillis() - startedAtMillis)

case class RoundEventFlags(
    watcher: Boolean,
    owner: Boolean,
    player: Option[chess.Color],
    moveBy: Option[chess.Color],
    troll: Boolean
)

opaque type UserTv = String
object UserTv extends OpaqueString[UserTv]

case class Clock(white: Int, black: Int)
/** A Go board, one char per point ('.', 'b', 'w'), as sent to mini boards. */
case class Position(lastMove: String, board: String, clock: Option[Clock], turnColor: Color)
