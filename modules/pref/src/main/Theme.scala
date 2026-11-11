package lila.pref

import play.api.libs.json.*

import lila.common.Json.given

case class Theme private[pref] (name: String, file: String, featured: Featured = Featured.No):

  override def toString = name

sealed trait ThemeObject:
  given Writes[Theme] = Json.writes[Theme]

  def default = all.head
  val all: List[Theme]

  lazy val allByName = all.mapBy(_.name)

  def apply(name: String): Theme = allByName.getOrElse(name, default)
  def apply(name: Option[String]): Theme = name.fold(default)(apply)

  def contains(name: String) = allByName contains name

/** Go board styles. `file` is unused: boards are drawn with CSS colors,
  * see ui/lib/css/go/_goban.scss.
  */
object Theme extends ThemeObject:

  val all = List(
    Theme("kaya", "", Featured.Yes),
    Theme("shin-kaya", "", Featured.Yes),
    Theme("katsura", "", Featured.Yes),
    Theme("walnut", "", Featured.Yes),
    Theme("bamboo", "", Featured.Yes),
    Theme("paper", "", Featured.Yes),
    Theme("slate", "", Featured.Yes),
    Theme("green", "", Featured.Yes),
    Theme("night", "", Featured.Yes)
  )
