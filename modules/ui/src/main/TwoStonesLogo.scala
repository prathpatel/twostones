package lila.ui

import ScalatagsTemplate.*

/** The TwoStones mark, drawn inline so its outline follows the site theme rather than the
  * system one: `.ts-mark .ink` in ui/lib/css/header/_title.scss. The same shape is in
  * public/logo/twostones-*.svg; bin/gen/favicons makes the PNGs from it.
  */
object TwoStonesLogo:

  def mark(size: Int, cls: String = "", label: Option[String] = None): Frag =
    def attrEscape(s: String) = s.replace("&", "&amp;").replace("\"", "&quot;").replace("<", "&lt;")
    val a11y = label.fold("""aria-hidden="true"""")(l => s"""role="img" aria-label="${attrEscape(l)}"""")
    raw:
      s"""<svg class="ts-mark $cls" width="$size" height="$size" viewBox="0 0 64 64" $a11y>""" +
        """<path class="ink" d="M29.42 6.35A22.36 22.36 0 0 0 29.42 50.77Z" fill="#141414" stroke-width="2.58" stroke-linejoin="round"/>""" +
        """<path class="ink" d="M34.58 13.23A22.36 22.36 0 0 1 34.58 57.65Z" fill="#f4f1ea" stroke-width="2.58" stroke-linejoin="round"/>""" +
        "</svg>"
