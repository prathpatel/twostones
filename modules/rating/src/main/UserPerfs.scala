package lila.rating

import chess.IntRating
import scalalib.HeapSort

import scala.collection.View

import lila.core.perf.{ KeyedPerf, Perf, UserPerfs }
import lila.core.user.LightPerf
import lila.rating.PerfExt.*

object UserPerfsExt:

  extension (ps: View[Perf]) def sumNb: Int = ps.foldLeft(0)(_ + _.nb)

  extension (p: UserPerfs)

    def perfsList: List[(PerfKey, Perf)] = List(
      PerfKey.ultraBullet -> p.ultraBullet,
      PerfKey.bullet -> p.bullet,
      PerfKey.blitz -> p.blitz,
      PerfKey.rapid -> p.rapid,
      PerfKey.classical -> p.classical,
      PerfKey.correspondence -> p.correspondence
    )

    def best8Perfs: List[PerfKey] = UserPerfs.firstRow ::: bestOf(UserPerfs.secondRow, 4)
    def best6Perfs: List[PerfKey] = UserPerfs.firstRow ::: bestOf(UserPerfs.secondRow, 2)
    def best4Perfs: List[PerfKey] = UserPerfs.firstRow
    def bestAny3Perfs: List[PerfKey] = bestOf(UserPerfs.firstRow ::: UserPerfs.secondRow, 3)
    def bestPerf: Option[PerfKey] = bestOf(UserPerfs.firstRow ::: UserPerfs.secondRow, 1).headOption
    private def bestOf(keys: List[PerfKey], nb: Int) = keys
      .sortBy: pk =>
        -(p(pk).nb * lila.rating.PerfType.totalTimeRoughEstimation.get(PerfType(pk)).so(_.roundSeconds.value))
      .take(nb)
    def hasEstablishedRating(pk: PerfKey) = p(pk).established

    def bestRatedPerf: Option[KeyedPerf] =
      val ps = perfsList.view.filter(p => p._1 != PerfKey.standard)
      val minNb = (ps.map(_._2).sumNb / 10).atLeast(1)
      ps.filter(_._2.nb >= minNb)
        .maxByOption(_._2.intRating)
        .map(KeyedPerf.apply)

    def bestPerfs(nb: Int): List[KeyedPerf] =
      val ps = PerfType.all.view.map(pt => pt.key -> p(pt))
      val minNb = (ps.map(_._2).sumNb / 15).atLeast(1)
      HeapSort.topNToList(ps.filter(p => p._2.nb >= minNb), nb).map(KeyedPerf.apply)

    def bestRating: IntRating = bestRatingIn(PerfType.leaderboardable)

    def bestStandardRating: IntRating = bestRatingIn(PerfType.standard)

    def bestRatingIn(types: List[PerfKey]): IntRating =
      val ps = types.nonEmptyOption.map(_.view.map(p(_))) | List(p.standard).view
      val minNb = ps.sumNb / 10
      val minGames = ps.filter(_.nb >= minNb)
      bestFromPerfs(minGames).intRating

    def bestFromPerfs(types: Iterable[Perf]): Perf =
      types.maxByOption(_.intRating) | lila.rating.Perf.default

    def bestPerf(types: List[PerfKey]): Perf = bestFromPerfs(types.view.map(p(_)))


  private given Ordering[IntRating] = intOrdering
  private given [A]: Ordering[(A, Perf)] = Ordering.by(_._2.intRating)

object UserPerfs:


  def default(id: UserId) =
    val p = lila.rating.Perf.default
    new UserPerfs(
      id,
      bullet = p,
      blitz = p,
      rapid = p,
      classical = p,
      correspondence = p,
      standard = p,
      ultraBullet = p
    )
  def defaultManaged(id: UserId) =
    val managed = lila.rating.Perf.defaultManaged
    default(id).copy(
      standard = managed,
      bullet = managed,
      blitz = managed,
      rapid = managed,
      classical = managed,
      correspondence = managed
    )

  def defaultBot(id: UserId) =
    val bot = lila.rating.Perf.defaultBot
    default(id).copy(
      standard = bot,
      bullet = bot,
      blitz = bot,
      rapid = bot,
      classical = bot,
      correspondence = bot
    )

  import lila.db.BSON
  import lila.db.dsl.given
  import reactivemongo.api.bson.*

  def idPerfReader(pk: PerfKey) = BSONDocumentReader.option[(UserId, Perf)] { doc =>
    import lila.rating.Perf.given
    for
      id <- doc.getAsOpt[UserId]("_id")
      perf <- doc.getAsOpt[Perf](pk.value)
    yield (id, perf)
  }

  given userPerfsHandler: BSONDocumentHandler[UserPerfs] = new BSON[UserPerfs]:

    import lila.rating.Perf.given

    def reads(r: BSON.Reader): UserPerfs =
      inline def perf(key: String) = r.getO[Perf](key).getOrElse(lila.rating.Perf.default)
      new UserPerfs(
        id = r.get[UserId]("_id"),
        standard = perf("standard"),
        ultraBullet = perf("ultraBullet"),
        bullet = perf("bullet"),
        blitz = perf("blitz"),
        rapid = perf("rapid"),
        classical = perf("classical"),
        correspondence = perf("correspondence")
      )

    private inline def notNew(p: Perf): Option[Perf] = p.nonEmpty.option(p)

    def writes(w: BSON.Writer, o: UserPerfs) =
      BSONDocument(
        "id" -> o.id,
        "standard" -> notNew(o.standard),
        "ultraBullet" -> notNew(o.ultraBullet),
        "bullet" -> notNew(o.bullet),
        "blitz" -> notNew(o.blitz),
        "rapid" -> notNew(o.rapid),
        "classical" -> notNew(o.classical),
        "correspondence" -> notNew(o.correspondence)
      )

  case class Leaderboards(
      ultraBullet: List[LightPerf],
      bullet: List[LightPerf],
      blitz: List[LightPerf],
      rapid: List[LightPerf],
      classical: List[LightPerf]
  )

  val emptyLeaderboards = Leaderboards(Nil, Nil, Nil, Nil, Nil)

  private[rating] val firstRow: List[PerfKey] =
    List(PerfKey.bullet, PerfKey.blitz, PerfKey.rapid, PerfKey.classical)
  private[rating] val secondRow: List[PerfKey] = List(
    PerfKey.correspondence,
    PerfKey.ultraBullet
  )
