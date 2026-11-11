package lila.web

import play.api.libs.json.Json

import lila.common.Json.given
import lila.core.config.NetConfig

final class StaticContent(net: NetConfig):

  val robotsTxt = """User-agent: *
Allow: /
Disallow: /game/
Disallow: /api/
"""

  val manifest = Json.obj(
    "name" -> net.domain,
    "short_name" -> "TwoStones",
    "start_url" -> "/",
    "display" -> "standalone",
    "background_color" -> "#161512",
    "theme_color" -> "#161512",
    "description" -> "A free, open source server for the game of Go.",
    "icons" -> List(32, 64, 128, 192, 256, 512).map: size =>
      Json.obj(
        "src" -> s"//${net.domain}/assets/logo/twostones-favicon-$size.png",
        "sizes" -> s"${size}x$size",
        "type" -> "image/png"
      )
  )
