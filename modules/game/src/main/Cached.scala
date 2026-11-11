package lila.game

import lila.db.dsl.*
import lila.memo.MongoCache

final class Cached(gameRepo: GameRepo, mongoCache: MongoCache.Api)(using Executor):

  def nbTotal: Fu[Long] = nbTotalCache.get {}

  private val nbTotalCache = mongoCache.unit[Long]("game:total", 29.minutes): loader =>
    _.refreshAfterWrite(30.minutes).buildAsyncFuture:
      loader: _ =>
        gameRepo.coll.secondary.countAll
