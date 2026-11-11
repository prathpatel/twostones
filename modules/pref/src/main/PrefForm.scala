package lila.pref

import play.api.data.*
import play.api.data.Forms.*

import lila.common.Form.{ into, numberIn, stringIn, tolerantBoolean }

object PrefForm:

  private def containedIn(choices: Seq[(Int, String)]): Int => Boolean =
    choice => choices.exists(_._1 == choice)

  private def checkedNumber(choices: Seq[(Int, String)]) =
    number.verifying(containedIn(choices))

  private def bitPresent(anInt: Int, bit: Int): Boolean =
    (anInt & bit) == bit

  private def bitContainedIn(choices: Seq[(Int, String)]): Int => Boolean =
    choice => choice == 0 || choices.exists((bit, _) => bitPresent(choice, bit))

  private def bitCheckedNumber(choices: Seq[(Int, String)]) =
    number.verifying(bitContainedIn(choices))

  private lazy val booleanNumber =
    number.verifying(Pref.BooleanPref.verify)

  object fields:
    val theme = "theme" -> text.verifying(Theme.contains(_))
    val pieceSet = "pieceSet" -> text.verifying(PieceSet.contains(_))
    val soundSet = "soundSet" -> text.verifying(SoundSet.contains(_))
    val bg = "bg" -> stringIn(Pref.Bg.fromString.keySet)
    val bgImg = "bgImg" -> text(maxLength = 400).verifying(
      "URL must use https",
      url => url.isBlank || url.startsWith("https://") || url.startsWith("//")
    )
    val zen = "zen" -> checkedNumber(Pref.Zen.choices)
    val voice = "voice" -> booleanNumber
    val takeback = "takeback" -> checkedNumber(Pref.Takeback.choices)
    val submitMove = "submitMove" -> bitCheckedNumber(Pref.SubmitMove.choices)
    val confirmResign = "confirmResign" -> checkedNumber(Pref.ConfirmResign.choices)
    val moretime = "moretime" -> checkedNumber(Pref.Moretime.choices)
    val clockSound = "clockSound" -> booleanNumber
    val clockTenths = "clockTenths" -> checkedNumber(Pref.ClockTenths.choices)
    val ratings = "ratings" -> checkedNumber(Pref.Ratings.choices)
    val flairs = "flairs" -> boolean
    val follow = "follow" -> booleanNumber
    val challenge = "challenge" -> checkedNumber(Pref.Challenge.choices)
    val message = "message" -> checkedNumber(Pref.Message.choices)
    object board:
      val brightness = "boardBrightness" -> number(20, 140)
      val contrast = "boardContrast" -> number(40, 200)
      val opacity = "boardOpacity" -> number(0, 100)
      val hue = "boardHue" -> number(0, 100)
    val sayGG = "sayGG" -> checkedNumber(Pref.SayGG.choices)
    val uiRoundness = "uiRoundness" -> number(0, 15)
    val bgOpacity = "bgOpacity" -> number(5, 100)
    val favFed =
      "favFed" -> optional(nonEmptyText(minLength = 2, maxLength = 6).into[lila.core.fide.Federation.Id])

  def pref(lichobile: Boolean) = Form(
    mapping(
      "display" -> mapping(
        "animation" -> optional(numberIn(Set(0, 1, 2, 3))),
        "captured" -> optional(booleanNumber),
        "highlight" -> optional(booleanNumber),
        "destination" -> optional(booleanNumber),
        "coords" -> checkedNumber(Pref.Coords.choices),
        "replay" -> optional(checkedNumber(Pref.Replay.choices)),
        fields.zen.map2(optional),
        "resizeHandle" -> optional(checkedNumber(Pref.ResizeHandle.choices))
      )(DisplayData.apply)(unapply),
      "behavior" -> mapping(
        "moveEvent" -> optional(numberIn(Set(0, 1, 2))),
        fields.takeback,
        fields.submitMove.map2: mapping =>
          if lichobile then
            import Pref.SubmitMove.lichobile as compat
            optional(numberIn(compat.choices).transform(compat.appToServer, compat.serverToApp))
          else optional(mapping),
        fields.confirmResign.map2(optional),
        fields.voice.map2(optional),
        fields.sayGG.map2(optional)
      )(BehaviorData.apply)(unapply),
      "clock" -> mapping(
        "tenths" -> checkedNumber(Pref.ClockTenths.choices),
        "bar" -> optional(booleanNumber),
        "sound" -> optional(booleanNumber),
        fields.moretime.map2(optional)
      )(ClockData.apply)(unapply),
      fields.follow,
      fields.challenge,
      fields.message,
      fields.ratings.map2(optional),
      fields.flairs.map2(optional)
    )(PrefData.apply)(unapply)
  )

  case class DisplayData(
      animation: Option[Int],
      captured: Option[Int],
      highlight: Option[Int],
      destination: Option[Int],
      coords: Int,
      replay: Option[Int],
      zen: Option[Int],
      resizeHandle: Option[Int]
  )

  case class BehaviorData(
      moveEvent: Option[Int],
      takeback: Int,
      submitMove: Option[Int],
      confirmResign: Option[Int],
      voice: Option[Int],
      sayGG: Option[Int]
  )

  case class ClockData(
      tenths: Int,
      bar: Option[Int],
      sound: Option[Int],
      moretime: Option[Int]
  )

  case class PrefData(
      display: DisplayData,
      behavior: BehaviorData,
      clock: ClockData,
      follow: Int,
      challenge: Int,
      message: Int,
      ratings: Option[Int],
      flairs: Option[Boolean]
  ):

    def apply(pref: Pref) =
      pref.copy(
        takeback = behavior.takeback,
        moretime = clock.moretime | pref.moretime,
        clockTenths = clock.tenths,
        clockBar = clock.bar.fold(pref.clockBar)(_ == 1),
        clockSound = clock.sound.fold(pref.clockSound)(_ == 1),
        follow = follow == 1,
        highlight = display.highlight.fold(pref.highlight)(_ == 1),
        destination = display.destination.fold(pref.destination)(_ == 1),
        coords = display.coords,
        replay = display.replay | pref.replay,
        challenge = challenge,
        message = message,
        animation = display.animation | pref.animation,
        submitMove = behavior.submitMove | pref.submitMove,
        confirmResign = behavior.confirmResign | pref.confirmResign,
        captured = display.captured.fold(pref.captured)(_ == 1),
        voice = if pref.voice.isEmpty && !behavior.voice.contains(1) then None else behavior.voice,
        zen = display.zen | pref.zen,
        ratings = ratings | pref.ratings,
        flairs = flairs | pref.flairs,
        resizeHandle = display.resizeHandle | pref.resizeHandle,
        sayGG = behavior.sayGG | pref.sayGG,
        moveEvent = behavior.moveEvent | pref.moveEvent
      )

  object PrefData:
    def apply(pref: Pref): PrefData =
      PrefData(
        display = DisplayData(
          highlight = (if pref.highlight then 1 else 0).some,
          destination = (if pref.destination then 1 else 0).some,
          animation = pref.animation.some,
          coords = pref.coords,
          replay = pref.replay.some,
          captured = (if pref.captured then 1 else 0).some,
          zen = pref.zen.some,
          resizeHandle = pref.resizeHandle.some
        ),
        behavior = BehaviorData(
          moveEvent = pref.moveEvent.some,
          takeback = pref.takeback,
          submitMove = pref.submitMove.some,
          confirmResign = pref.confirmResign.some,
          voice = pref.voice.getOrElse(0).some,
          sayGG = pref.sayGG.some
        ),
        clock = ClockData(
          tenths = pref.clockTenths,
          bar = (if pref.clockBar then 1 else 0).some,
          sound = (if pref.clockSound then 1 else 0).some,
          moretime = pref.moretime.some
        ),
        follow = if pref.follow then 1 else 0,
        challenge = pref.challenge,
        message = pref.message,
        ratings = pref.ratings.some,
        flairs = pref.flairs.some
      )

  def prefOf(p: Pref): Form[PrefData] = pref(lichobile = false).fill(PrefData(p))

  val cfRoutingForm = Form(single("cfRouting" -> boolean))
