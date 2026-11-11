package lila.pref
package ui

import play.api.data.Form

import lila.ui.*
import lila.ui.ScalatagsTemplate.{ *, given }

final class AccountPref(helpers: Helpers, helper: PrefHelper, bits: AccountUi):
  import helpers.{ *, given }
  import helper.*
  import bits.*
  import trans.preferences as trp

  private def categFieldset(categ: lila.pref.PrefCateg, active: lila.pref.PrefCateg) =
    div(cls := List("none" -> (categ != active)))

  // the Go board shows coordinates around the board, or not at all
  private def goCoordinateChoices(using Translate) =
    List((Pref.Coords.NONE, trans.site.no.txt()), (Pref.Coords.INSIDE, trans.site.yes.txt()))

  private def setting(name: Frag, body: Frag, settingId: String) =
    st.section(a(href := "#" + settingId)(h2(id := settingId)(name)), body)

  def apply(u: User, form: play.api.data.Form[?], categ: lila.pref.PrefCateg)(using Context) =
    AccountPage(
      s"${bits.categName(categ)} - ${u.username} - ${trans.preferences.preferences.txt()}",
      categ.slug
    ):
      val booleanChoices = translatedBooleanIntChoices
      div(cls := "box box-pad")(
        h1(cls := "box__top")(bits.categName(categ)),
        postForm(cls := "autosubmit", action := routes.Pref.formApply)(
          categFieldset(PrefCateg.Display, categ)(
            setting(
              "Board coordinates",
              radios(form("display.coords"), goCoordinateChoices),
              "boardCoordinates"
            ),
            setting(
              trp.zenMode(),
              radios(form("display.zen"), translatedZenChoices),
              "zenMode"
            ),
            setting(
              trp.showPlayerRatings(),
              frag(
                radios(form("ratings"), translatedRatingsChoices),
                div(cls := "help text shy", dataIcon := Icon.InfoCircle)(
                  "Hides all ratings from the site, to help you focus on the game. ",
                  "Rated games still change your rating."
                )
              ),
              "showRatings"
            ),
            setting(
              trp.showFlairs(),
              radios(form("flairs"), translatedBooleanChoices),
              "showFlairs"
            )
          ),
          categFieldset(PrefCateg.Clock, categ)(
            setting(
              trp.tenthsOfSeconds(),
              radios(form("clock.tenths"), translatedClockTenthsChoices),
              "tenthsOfSeconds"
            )
          ),
          categFieldset(PrefCateg.GameBehavior, categ)(
            setting(
              trp.takebacksWithOpponentApproval(),
              radios(form("behavior.takeback"), translatedTakebackChoices),
              "takebacksWithOpponentApproval"
            ),
            setting(
              trp.sayGgWpAfterLosingOrDrawing(),
              radios(form("behavior.sayGG"), translatedSayGGChoices),
              "sayGG"
            )
          ),
          categFieldset(PrefCateg.Privacy, categ)(
            setting(
              trans.site.letOtherPlayersFollowYou(),
              radios(form("follow"), booleanChoices),
              "letOtherPlayersFollowYou"
            ),
            setting(
              trans.site.letOtherPlayersChallengeYou(),
              radios(form("challenge"), translatedChallengeChoices),
              "letOtherPlayersChallengeYou"
            ),
            setting(
              trans.site.letOtherPlayersMessageYou(),
              radios(form("message"), translatedMessageChoices),
              "letOtherPlayersMessageYou"
            )
          ),
          p(cls := "saved text none", dataIcon := Icon.Checkmark)(trp.yourPreferencesHaveBeenSaved())
        )
      )

  object notification:

    def apply(form: Form[?])(using Context) =
      AccountPage(
        s"${trans.preferences.notifications.txt()} - ${trp.preferences.txt()}",
        "notification"
      ):
        div(cls := "box box-pad")(
          h1(cls := "box__top")(trans.preferences.notifications()),
          postForm(cls := "autosubmit", action := routes.Pref.notifyFormApply)(
            div(
              table(cls := "allows")(
                thead(
                  tr(
                    th,
                    th("On the site", iconTag(Icon.BellOutline)),
                    th("On your device, when you're away", iconTag(Icon.PhoneMobile))
                  )
                ),
                tbody(
                  List(
                    trp.notifyDirectMessage() -> "privateMessage",
                    trp.notifyChallenge() -> "challenge",
                    trp.notifyGameEvent() -> "gameEvent"
                  ).map(makeRow(form))
                )
              ),
              div(
                id := "correspondence-email-notif"
              )( // id is set to allow direct unsubcribe link in correspondence emails
                bits.setting(
                  trp.correspondenceEmailNotification(),
                  radios(form("correspondenceEmail"), translatedBooleanChoices)
                )
              ),
              bits.setting(
                trp.bellNotificationSound(),
                radios(form("notification.playBellSound"), translatedBooleanIntChoices)
              )
            ),
            p(cls := "saved text none", dataIcon := Icon.Checkmark)(trp.yourPreferencesHaveBeenSaved())
          )
        )

    private def makeRow(form: play.api.data.Form[?])(transFrag: Frag, filterName: String) =
      tr(
        td(transFrag),
        Seq("bell", "push").map: allow =>
          val name = s"$filterName.$allow"
          val checked = form.data(name).contains("true")
          td(
            if !hiddenFields(s"$filterName.$allow") then
              div(cls := "toggle", form3.nativeCheckbox(name, name, checked))
            else if !checked then div(iconTag(Icon.X))
            else
              div(
                cls := "always-on",
                form3.hidden(name, "true"),
                filterName match
                  case "challenge" => iconTag(Icon.Swords)
                  case "privateMessage" => iconTag(Icon.BellOutline)
                  case _ => emptyFrag
              )
          )
      )

    private val hiddenFields = Set(
      "privateMessage.bell",
      "tournamentSoon.bell",
      "gameEvent.bell",
      "challenge.bell"
    )

  def network(cfRouting: Boolean)(using ctx: Context) =
    AccountPage(trp.network.txt(), "network"):
      div(cls := "box box-pad")(
        standardFlash,
        h1(cls := "box__top")(trp.network()),
        flashMessage("quiet")(
          if cfRouting then trp.youAreCurrentlyUsingCdnRouting()
          else trp.youAreCurrentlyUsingDirectRouting(),
          br,
          trp.frequentDisconnectsAdvice()
        ),
        br,
        postForm(action := routes.Pref.networkPost):
          button(
            name := "cfRouting",
            value := !cfRouting,
            cls := "button"
          )(if cfRouting then trp.useDirectRouting() else trp.useCdnRouting())
      )
