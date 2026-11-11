package lila.pref

import play.api.libs.json.*

import lila.common.Json.given

def toJson(p: Pref) = Json
  .obj(
    "bgImg" -> p.bgImgUrl,
    "theme" -> p.theme,
    "pieceSet" -> p.pieceSet,
    "soundSet" -> p.soundSet,
    "takeback" -> p.takeback,
    "moretime" -> p.moretime,
    "clockTenths" -> p.clockTenths,
    "clockBar" -> p.clockBar,
    "clockSound" -> p.clockSound,
    "animation" -> p.animation,
    "captured" -> p.captured,
    "follow" -> p.follow,
    "highlight" -> p.highlight,
    "destination" -> p.destination,
    "coords" -> p.coords,
    "replay" -> p.replay,
    "challenge" -> p.challenge,
    "message" -> p.message,
    "submitMove" -> p.submitMove,
    "confirmResign" -> p.confirmResign,
    "voiceMove" -> p.hasVoice,
    "zen" -> p.zen,
    "ratings" -> p.ratings,
    "moveEvent" -> p.moveEvent,
    "flairs" -> p.flairs,
    "sayGG" -> p.sayGG
  )
  .add("favFed", p.favFed)

def lichobile(p: Pref) = Json.obj(
  "animation" -> p.animation,
  "captured" -> p.captured,
  "highlight" -> p.highlight,
  "destination" -> p.destination,
  "coords" -> p.coords,
  "replay" -> p.replay,
  "takeback" -> p.takeback,
  "confirmResign" -> p.confirmResign,
  "moretime" -> p.moretime,
  "clockTenths" -> p.clockTenths,
  "clockBar" -> p.clockBar,
  "clockSound" -> p.clockSound,
  "follow" -> p.follow,
  "challenge" -> p.challenge,
  "message" -> p.message,
  "submitMove" -> Pref.SubmitMove.lichobile.serverToApp(p.submitMove)
)
