package lila.web

import com.softwaremill.macwire.*
import play.api.libs.ws.StandaloneWSClient

@Module
final class Env(
    appConfig: play.api.Configuration,
    cacheApi: lila.memo.CacheApi,
    settingStore: lila.memo.SettingStore.Builder,
    ws: StandaloneWSClient,
    net: lila.core.config.NetConfig,
    getFile: lila.common.config.GetRelativeFile
)(using mode: play.api.Mode, scheduler: Scheduler)(using Executor):

  val config = WebConfig.loadFrom(appConfig)
  export config.pagerDuty as pagerDutyConfig
  export net.baseUrl

  val analyseEndpoints = WebConfig.analyseEndpoints(appConfig)
  lazy val lilaVersion = WebConfig.LilaVersion.read(appConfig)

  val manifest = wire[AssetManifest]

  val referrerRedirect = wire[ReferrerRedirect]

  val github = wire[GitHub]

  val static = wire[StaticContent]

  lazy val emailError = wire[EmailError]

  lazy val t3AuthMonitor = T3AuthMonitor()

  private lazy val influxEvent = InfluxEvent(
    ws = ws,
    endpoint = config.influxEventEndpoint,
    env = config.influxEventEnv
  )
  if mode.isProd && config.influxEventEndpoint.nonEmpty then
    scheduler.scheduleOnce(5.seconds)(influxEvent.start())

  wire[PagerDuty]

  val lichobileAnnounceApi = wire[LichobileAnnounceApi]

  val playerCounter = wire[PlayerCounter]

  AnnounceApi.setupPeriodicUpdate()
  PrometheusReporter.setupPeriodicMonitor()

  object settings:
    import lila.core.data.Strings
    import lila.memo.SettingStore.Strings.given

    val noDelaySecret = settingStore[Strings](
      "noDelaySecrets",
      default = Strings(Nil),
      text = "Secret tokens that allows fetching ongoing games without the delay. Separated by commas.".some
    )
