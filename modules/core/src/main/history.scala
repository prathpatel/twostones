package lila.core
package history

import _root_.chess.IntRating
import scalalib.model.Days

import lila.core.perf.PerfKey
import lila.core.user.{ User, WithPerf }
import lila.core.userId.UserId

trait HistoryApi:
  def progresses(users: List[WithPerf], perfKey: PerfKey, days: Days): Fu[List[PairOf[IntRating]]]
  def lastWeekTopRating(user: UserId, perf: PerfKey): Fu[IntRating]
  def setPerfRating(user: User, perf: PerfKey, rating: IntRating): Funit
