package lila.ws

import java.util.concurrent.atomic.AtomicReference

import ipc.*

final class KeepAlive(lila: Lila, scheduler: Scheduler)(using Executor):

  import KeepAlive.*

  val challenge = new AliveRooms

  scheduler.scheduleWithFixedDelay(15.seconds, 15.seconds) { () =>
    lila.emit.challenge(challenge.getAndClear)
  }

object KeepAlive:

  type Seconds = Int

  final class AliveRooms:

    private val rooms: AtomicReference[Set[RoomId]] = AtomicReference(Set.empty)

    def apply(roomId: RoomId): Unit = rooms.getAndUpdate(_ + roomId)

    def getAndClear: LilaIn.KeepAlives = LilaIn.KeepAlives(rooms.getAndUpdate(_ => Set.empty))
