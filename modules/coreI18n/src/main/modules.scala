package lila.core.i18n

enum I18nModule:
  case site, arena, emails, learn, activity, coordinates, study, `class`, contact, patron, coach,
    broadcast, streamer, tfa, settings, preferences, team, perfStat, search, faq, lag, swiss, puzzle,
    puzzleTheme, challenge, storm, ublog, timeago, oauthScope, dgt, voiceCommands, onboarding,
    features, nvui, variant, recap, localAnalysis, report
object I18nModule:
  type Selector = I18nModule.type => I18nModule
