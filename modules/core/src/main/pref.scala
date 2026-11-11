package lila.core
package pref

import lila.core.user.User
import lila.core.userId.UserId

trait Pref:
  val id: UserId
  val coords: Int
  val voice: Option[Int]
  val animation: Int
  val destination: Boolean
  val moveEvent: Int
  val highlight: Boolean
  val resizeHandle: Int
  val uiRoundness: Int
  val theme: String
  val pieceSet: String
  val usingAltSocket: Option[Boolean]
  val blogFilter: ublog.QualityFilter
  val bg: Int

  def hasVoice: Boolean
  def hasSpeech: Boolean
  def hideRatingsInGame: Boolean
  def showRatings: Boolean
  def animationMillis: Int
  def bgOpacity: Int

trait PrefApi:
  def followable(userId: UserId): Fu[Boolean]
  def mentionableIds(userIds: Set[UserId]): Fu[Set[UserId]]
  def getMessage(userId: UserId): Fu[Int]
  def getChallenge(userId: UserId): Fu[Int]
  def isolate(user: User): Funit

object Message:
  val NEVER = 1
  val FRIEND = 2
  val ALWAYS = 3

object Challenge:
  val NEVER = 1
  val RATING = 2
  val FRIEND = 3
  val REGISTERED = 4
  val ALWAYS = 5

