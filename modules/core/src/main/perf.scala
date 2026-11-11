package lila.core

import _root_.chess.rating.IntRatingDiff
import _root_.chess.rating.glicko.Glicko
import _root_.chess.variant.Variant
import _root_.chess.{ IntRating, Speed }
import monocle.syntax.AppliedPLens
import monocle.syntax.all.*

import lila.core.userId.{ UserId, UserIdOf }

object perf:

  opaque type PerfKey = String
  object PerfKey:
    val bullet: PerfKey = "bullet"
    val blitz: PerfKey = "blitz"
    val rapid: PerfKey = "rapid"
    val classical: PerfKey = "classical"
    val correspondence: PerfKey = "correspondence"
    val standard: PerfKey = "standard"
    val ultraBullet: PerfKey = "ultraBullet"
    val list: List[PerfKey] = List(
      bullet,
      blitz,
      rapid,
      classical,
      correspondence,
      standard,
      ultraBullet
    )
    val all: Set[PerfKey] = list.toSet
    def keyIdMap: Map[PerfKey, PerfId] = Map(
      ultraBullet -> 0,
      bullet -> 1,
      blitz -> 2,
      rapid -> 6,
      classical -> 3,
      correspondence -> 4,
      standard -> 5
    )

    extension (key: PerfKey)
      def value: String = key
      def id: PerfId = keyIdMap(key)

    given Show[PerfKey] = _.value
    given SameRuntime[PerfKey, String] = _.value
    given Eq[PerfKey] = Eq.by(_.value)

    def apply(key: String): Option[PerfKey] = Option.when(all.contains(key))(key)

    /** Go has no variants: the speed alone picks the rating. */
    def apply(@annotation.nowarn variant: Variant, speed: Speed): PerfKey = standardBySpeed(speed)

    def standardBySpeed(speed: Speed): PerfKey = speed match
      case Speed.Bullet => bullet
      case Speed.Blitz => blitz
      case Speed.Rapid => rapid
      case Speed.Classical => classical
      case Speed.Correspondence => correspondence
      case Speed.UltraBullet => ultraBullet

  opaque type PerfId = Int
  object PerfId extends OpaqueInt[PerfId]

  trait PerfStatApi:
    def highestRating(user: UserId, perfKey: PerfKey): Fu[Option[IntRating]]

  case class Perf(glicko: Glicko, nb: Int, recent: List[IntRating], latest: Option[Instant]):
    export glicko.{ intRating, intDeviation, provisional }
    export latest.{ isEmpty, nonEmpty }

    def keyed(key: PerfKey) = KeyedPerf(key, this)

    def progress: IntRatingDiff = {
      for
        head <- recent.headOption
        last <- recent.lastOption
      yield IntRatingDiff(head.value - last.value)
    } | IntRatingDiff(0)

  case class KeyedPerf(key: PerfKey, perf: Perf)

  case class UserPerfs(
      id: UserId,
      bullet: Perf,
      blitz: Perf,
      rapid: Perf,
      classical: Perf,
      correspondence: Perf,
      standard: Perf,
      ultraBullet: Perf
  ):
    def apply(key: PerfKey): Perf = key match
      case "bullet" => bullet
      case "blitz" => blitz
      case "rapid" => rapid
      case "classical" => classical
      case "correspondence" => correspondence
      case "ultraBullet" => ultraBullet
      case "standard" => standard
      // impossible because PerfKey can't be instantiated with arbitrary values
      case key => sys.error(s"Unknown perf key: $key")

    def keyed(key: PerfKey): KeyedPerf = KeyedPerf(key, apply(key))

    def focusKey(key: PerfKey): AppliedPLens[UserPerfs, UserPerfs, Perf, Perf] = key match
      case "bullet" => this.focus(_.bullet)
      case "blitz" => this.focus(_.blitz)
      case "rapid" => this.focus(_.rapid)
      case "classical" => this.focus(_.classical)
      case "correspondence" => this.focus(_.correspondence)
      case "ultraBullet" => this.focus(_.ultraBullet)
      case "standard" => this.focus(_.standard)
      // impossible because PerfKey can't be instantiated with arbitrary values
      case key => sys.error(s"Unknown perf key: $key")

  case class UserWithPerfs(user: lila.core.user.User, perfs: UserPerfs):
    export user.*
  object UserWithPerfs:
    given UserIdOf[UserWithPerfs] = _.id
