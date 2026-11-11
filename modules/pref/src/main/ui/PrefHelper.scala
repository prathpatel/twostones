package lila
package pref

import lila.core.i18n.{ I18nKey as trans, Translate }

trait PrefHelper:

  def translatedZenChoices(using Translate) =
    List(
      (Pref.Zen.NO, trans.site.no.txt()),
      (Pref.Zen.YES, trans.site.yes.txt()),
      (Pref.Zen.GAME_AUTO, trans.preferences.inGameOnly.txt())
    )

  def translatedRatingsChoices(using Translate) =
    List(
      (Pref.Ratings.NO, trans.site.no.txt()),
      (Pref.Ratings.YES, trans.site.yes.txt()),
      (Pref.Ratings.EXCEPT_GAME, trans.preferences.exceptInGame.txt())
    )

  def translatedClockTenthsChoices(using Translate) =
    List(
      (Pref.ClockTenths.NEVER, trans.site.never.txt()),
      (Pref.ClockTenths.LOWTIME, trans.preferences.whenTimeRemainingLessThanTenSeconds.txt()),
      (Pref.ClockTenths.ALWAYS, trans.site.always.txt())
    )

  def translatedTakebackChoices(using Translate) =
    List(
      (Pref.Takeback.NEVER, trans.site.never.txt()),
      (Pref.Takeback.ALWAYS, trans.site.always.txt()),
      (Pref.Takeback.CASUAL, trans.preferences.inCasualGamesOnly.txt())
    )

  def translatedChallengeChoices(using Translate) =
    List(
      (lila.core.pref.Challenge.NEVER, trans.site.never.txt()),
      (
        lila.core.pref.Challenge.RATING,
        trans.site.ifRatingIsPlusMinusX.txt(Pref.Challenge.ratingThreshold)
      ),
      (lila.core.pref.Challenge.FRIEND, trans.site.onlyFriends.txt()),
      (lila.core.pref.Challenge.REGISTERED, trans.site.ifRegistered.txt()),
      (lila.core.pref.Challenge.ALWAYS, trans.site.always.txt())
    )

  def translatedMessageChoices(using Translate) =
    List(
      (lila.core.pref.Message.NEVER, trans.site.onlyExistingConversations.txt()),
      (lila.core.pref.Message.FRIEND, trans.site.onlyFriends.txt()),
      (lila.core.pref.Message.ALWAYS, trans.site.always.txt())
    )


  def translatedSayGGChoices(using Translate) =
    List(
      (Pref.SayGG.NO, trans.site.never.txt()),
      (Pref.SayGG.DEFEAT, trans.site.defeatOnly.txt()),
      (Pref.SayGG.DRAW, trans.site.drawAndDefeat.txt())
    )
