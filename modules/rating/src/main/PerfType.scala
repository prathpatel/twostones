package lila.rating

import cats.derived.*
import chess.{ Centis, Speed }

import lila.core.i18n.{ I18nKey, Translate }
import lila.core.perf.PerfId
import lila.ui.Icon

enum PerfType(
    val id: PerfId,
    val key: PerfKey,
    val icon: Icon,
    val nameKey: I18nKey,
    val descKey: I18nKey
) derives Eq:

  def trans(using translate: Translate): String = nameKey.txt()
  def desc(using translate: Translate): String = descKey.txt()

  case UltraBullet
      extends PerfType(
        PerfId(0),
        key = PerfKey.ultraBullet,
        icon = Icon.UltraBullet,
        nameKey = I18nKey.site.ultraBullet,
        descKey = I18nKey.site.ultraBulletDesc
      )

  case Bullet
      extends PerfType(
        PerfId(1),
        key = PerfKey.bullet,
        icon = Icon.Bullet,
        nameKey = I18nKey.site.bullet,
        descKey = I18nKey.site.bulletDesc
      )

  case Blitz
      extends PerfType(
        PerfId(2),
        key = PerfKey.blitz,
        icon = Icon.FlameBlitz,
        nameKey = I18nKey.site.blitz,
        descKey = I18nKey.site.blitzDesc
      )

  case Rapid
      extends PerfType(
        PerfId(6),
        key = PerfKey.rapid,
        icon = Icon.Rabbit,
        nameKey = I18nKey.site.rapid,
        descKey = I18nKey.site.rapidDesc
      )

  case Classical
      extends PerfType(
        PerfId(3),
        key = PerfKey.classical,
        icon = Icon.Turtle,
        nameKey = I18nKey.site.classical,
        descKey = I18nKey.site.classicalDesc
      )

  case Correspondence
      extends PerfType(
        PerfId(4),
        key = PerfKey.correspondence,
        icon = Icon.PaperAirplane,
        nameKey = I18nKey.site.correspondence,
        descKey = I18nKey.site.correspondenceDesc
      )

  case Standard
      extends PerfType(
        PerfId(5),
        key = PerfKey.standard,
        icon = Icon.Crown,
        nameKey = I18nKey.variant.standard,
        descKey = I18nKey.variant.standardTitle
      )

object PerfType:

  // all but standard
  type GamePerf = Bullet.type | Blitz.type | Rapid.type | Classical.type | UltraBullet.type |
    Correspondence.type

  def gamePerf(pt: PerfType): Option[GamePerf] = pt match
    case gp: GamePerf => Some(gp)
    case _ => None

  given Conversion[PerfType, PerfKey] = _.key
  given Conversion[PerfType, PerfId] = _.id
  given Conversion[PerfKey, PerfType] = apply(_)

  val all: List[PerfType] = values.toList
  val byKey = all.mapBy(_.key)
  val byId = all.mapBy(_.id)

  def apply(key: PerfKey): PerfType =
    byKey.getOrElse(key, sys.error(s"Impossible: $key couldn't have been instantiated"))

  def apply(id: PerfId): Option[PerfType] = byId.get(id)

  val standard: List[PerfKey] =
    List(PerfKey.bullet, PerfKey.blitz, PerfKey.rapid, PerfKey.classical, PerfKey.correspondence)
  val standardSet: Set[PerfKey] = standard.toSet
  val standardWithUltra: List[PerfKey] = PerfKey.ultraBullet :: standard
  val leaderboardable: List[PerfKey] = List(
    PerfKey.bullet,
    PerfKey.blitz,
    PerfKey.rapid,
    PerfKey.classical,
    PerfKey.ultraBullet
  )
  val isLeaderboardable: Set[PerfKey] = leaderboardable.toSet

  def apply(variant: chess.variant.Variant, speed: Speed): PerfType = PerfType(PerfKey(variant, speed))

  lazy val totalTimeRoughEstimation: Map[PerfType, Centis] =
    all.view
      .map: pt =>
        pt -> Centis:
          pt.match
            case UltraBullet => 25 * 100
            case Bullet => 90 * 100
            case Blitz => 7 * 60 * 100
            case Rapid => 12 * 60 * 100
            case Classical => 30 * 60 * 100
            case Correspondence => 60 * 60 * 100
            case _ => 7 * 60 * 100
      .to(Map)

  val translated: Set[PerfType] = Set(Bullet, Blitz, Rapid, Classical, Correspondence)
