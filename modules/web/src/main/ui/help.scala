package lila.web
package ui

import lila.core.i18n.{ I18nKey as trans, Translate }
import lila.ui.*
import lila.ui.ScalatagsTemplate.*

object help:

  private def header(text: Frag) = tr(th(colspan := 2)(p(text)))
  private def row(keys: Frag, desc: Frag) = tr(td(cls := "keys")(keys), td(cls := "desc")(desc))
  private val or = tag("or")("/")
  private val alt = span(cls := "kbd-mod")("or")
  private val kbd = tag("kbd")

  private def navigateMoves(using Translate) = frag(
    header(trans.site.moveListNavigation()),
    row(
      frag(kbd("←"), or, kbd("→"), alt, kbd("k"), or, kbd("j")),
      trans.site.keyMoveBackwardOrForward()
    ),
    row(
      frag(kbd("↑"), or, kbd("↓"), alt, kbd("0"), or, kbd("$"), alt, kbd("home"), or, kbd("end")),
      trans.site.keyGoToStartOrEnd()
    )
  )

  /** The keyboard shortcuts of the game page. */
  def round(hasChat: Boolean)(using Translate) =
    frag(
      h2(trans.site.keyboardShortcuts()),
      table(
        tbody(
          navigateMoves,
          header(trans.site.other()),
          row(kbd("f"), trans.site.flipBoard()),
          row(kbd("z"), trans.preferences.zenMode()),
          hasChat.option(
            row(kbd("c"), trans.site.focusChat())
          ),
          row(kbd("h"), trans.site.menu()),
          row(kbd("?"), trans.site.showHelpDialog())
        )
      )
    )
