package lila.core
package chat

import play.api.libs.json.JsObject

import lila.core.data.Url
import lila.core.id.*
import lila.core.userId.*

case class ChatLine(chatId: ChatId, line: Line, json: JsObject)
case class OnTimeout(chatId: ChatId, userId: UserId)
case class OnReinstate(chatId: ChatId, userId: UserId)

trait Line:
  def text: String
  def author: String
  def deleted: Boolean
  def isSystem = author == UserName.lichess.value
  def isHuman = !isSystem
  def humanAuthor = isHuman.option(author)
  def troll: Boolean
  def userIdMaybe: Option[UserId]

enum BusChan:
  val chan = s"chat:$toString"
  case round
  case tournament
  case simul
  case study
  case team
  case swiss
  case global
object BusChan:
  type Select = BusChan.type => BusChan

enum PublicSource(val typeName: String, val someId: Any):
  case Tournament(id: TourId) extends PublicSource("tournament", id)
  case Simul(id: SimulId) extends PublicSource("simul", id)
  case Study(id: StudyId) extends PublicSource("study", id)
  case Player(gameId: GameId) extends PublicSource("player", id) // not actually public
  case Watcher(gameId: GameId) extends PublicSource("watcher", id)
  case Team(id: TeamId) extends PublicSource("team", id)
  case Swiss(id: SwissId) extends PublicSource("swiss", id)
  case Forum(id: ForumPostId) extends PublicSource("forum", id)
  case Ublog(id: UblogPostId) extends PublicSource("ublog", id)
  case Relay(id: RelayRoundId) extends PublicSource("relay", id)

  def resourceId: ResourceId = ResourceId:
    this match
      case PublicSource.Watcher(gameId) => s"game/$gameId"
      case PublicSource.Player(gameId) => gameId.value
      case _ => s"$typeName/$someId"

object PublicSource:
  object longNotation:
    def read(str: String): Option[PublicSource] = str.split('/') match
      case Array("tournament", id) => Some(PublicSource.Tournament(TourId(id)))
      case Array("simul", id) => Some(PublicSource.Simul(SimulId(id)))
      case Array("game", id) => Some(PublicSource.Watcher(GameId(id)))
      case Array("study", id) => Some(PublicSource.Study(StudyId(id)))
      case Array("team", id) => Some(PublicSource.Team(TeamId(id)))
      case Array("swiss", id) => Some(PublicSource.Swiss(SwissId(id)))
      case Array("forum", id) => Some(PublicSource.Forum(ForumPostId(id)))
      case Array("blog", id) => Some(PublicSource.Ublog(UblogPostId(id)))
      case Array("relay", id) => Some(PublicSource.Relay(RelayRoundId(id)))
      case _ => None

opaque type ResourceId = String
object ResourceId extends OpaqueString[ResourceId]

enum TimeoutReason(val key: String, val name: String):
  lazy val shortName = name.split(';').headOption | name
  case PublicShaming extends TimeoutReason("shaming", "public shaming; please use the report form instead")
  case Insult extends TimeoutReason("insult", "disrespecting other players; see the Terms of Service")
  case Spam extends TimeoutReason("spam", "spamming the chat; see the Terms of Service")
  case Other extends TimeoutReason("other", "inappropriate behavior; see the Terms of Service")
object TimeoutReason:
  val all = values.toList
  def apply(key: String) = all.find(_.key == key)

enum TimeoutScope:
  case Local, Global

trait ChatApi:
  def exists(chatId: ChatId): Fu[Boolean]
  def write(
      chatId: ChatId,
      userId: UserId,
      text: String,
      publicSource: Option[PublicSource],
      busChan: BusChan.Select,
      persist: Boolean = true
  ): Funit
  def volatile(chatId: ChatId, text: String, busChan: BusChan.Select): Unit
  def system(chatId: ChatId, text: String, busChan: BusChan.Select): Funit
  def timeout(
      chatId: ChatId,
      userId: UserId,
      reason: TimeoutReason,
      scope: TimeoutScope,
      text: String,
      busChan: BusChan.Select
  )(using MyId): Funit
