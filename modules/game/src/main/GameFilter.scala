package lila.game

enum GameFilter:
  val name = toString
  case all, me, rated, win, loss, draw, playing, bookmark, search

object GameFilter:

  val list: NonEmptyList[GameFilter] =
    NonEmptyList.of(all, me, rated, win, loss, draw, playing, bookmark, search)

  def apply(name: String) =
    list.find(_.name == name) | list.head

case class GameFilterMenu(
    all: NonEmptyList[GameFilter],
    current: GameFilter
):
  def list = all.toList
