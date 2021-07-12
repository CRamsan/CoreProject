
import com.cramsan.framework.assertlib.AssertUtil
import com.cramsan.framework.assertlib.AssertUtilInterface
import com.cramsan.framework.assertlib.implementation.AssertUtilImpl
import com.cramsan.framework.halt.HaltUtil
import com.cramsan.framework.halt.HaltUtilDelegate
import com.cramsan.framework.halt.implementation.HaltUtilImpl
import com.cramsan.framework.logging.EventLogger
import com.cramsan.framework.logging.EventLoggerDelegate
import com.cramsan.framework.logging.EventLoggerErrorCallback
import com.cramsan.framework.logging.EventLoggerErrorCallbackDelegate
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.implementation.EventLoggerErrorCallbackImpl
import com.cramsan.framework.logging.implementation.EventLoggerImpl
import com.cramsan.framework.metrics.Metrics
import com.cramsan.framework.metrics.MetricsDelegate
import com.cramsan.framework.metrics.MetricsInterface
import com.cramsan.framework.metrics.implementation.MetricsImpl
import com.cramsan.framework.thread.RunBlock
import com.cramsan.framework.thread.ThreadUtil
import com.cramsan.framework.thread.ThreadUtilDelegate
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.framework.thread.implementation.ThreadUtilImpl
import com.cramsan.ps2link.appcore.census.DBGCensus
import com.cramsan.ps2link.appcore.census.DBGServiceClient
import com.cramsan.ps2link.appcore.census.DBGServiceClientImpl
import com.cramsan.ps2link.appcore.census.buildHttpClient
import com.cramsan.ps2link.appcore.network.HttpClient
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import com.cramsan.ps2link.appcore.repository.PS2LinkRepositoryImpl
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json

object PS2ServiceModule {

    fun provideHaltUtilDelegate(): HaltUtilDelegate = object : HaltUtilDelegate {
        override fun resumeThread() = Unit

        override fun stopThread() = Unit

        override fun crashApp() = Unit
    }

    fun provideHaltUtilInterface(haltUtilDelegate: HaltUtilDelegate): HaltUtil =
        HaltUtilImpl(haltUtilDelegate)

    fun provideEventLoggerErrorCallbackDelegate(): EventLoggerErrorCallbackDelegate =
        object : EventLoggerErrorCallbackDelegate {
            override fun handleErrorEvent(
                tag: String,
                message: String,
                throwable: Throwable,
                severity: Severity,
            ) = Unit
        }

    fun provideEventLoggerErrorCallback(
        eventLoggerDelegate: EventLoggerDelegate,
        delegate: EventLoggerErrorCallbackDelegate,
    ): EventLoggerErrorCallback =
        EventLoggerErrorCallbackImpl(eventLoggerDelegate, delegate)

    fun provideEventLoggerDelegate(): EventLoggerDelegate = object : EventLoggerDelegate {
        override fun log(severity: Severity, tag: String, message: String, throwable: Throwable?) {
            val formattedMessage = "[$tag] $message"
            when (severity) {
                Severity.VERBOSE -> console.log(formattedMessage)
                Severity.DEBUG -> console.log(formattedMessage)
                Severity.INFO -> console.info(formattedMessage)
                Severity.WARNING -> console.warn(formattedMessage, throwable)
                Severity.ERROR -> console.error(formattedMessage, throwable)
            }
        }
    }

    fun provideEventLoggerInterface(
        eventLoggerDelegate: EventLoggerDelegate,
    ): EventLoggerInterface {
        val instance =
            EventLoggerImpl(Severity.DEBUG, null, eventLoggerDelegate)
        return EventLogger.instance(instance)
    }

    fun provideAssertUtil(
        eventLoggerInterface: EventLoggerInterface,
        haltUtilInterface: HaltUtil,
    ): AssertUtilInterface {
        val impl = AssertUtilImpl(
            haltOnFailure = true,
            eventLoggerInterface,
            haltUtilInterface
        )
        return AssertUtil.instance(impl)
    }

    fun provideThreadUtilDelegate(
        assertUtilInterface: AssertUtilInterface,
    ): ThreadUtilDelegate {
        return object : ThreadUtilDelegate {
            override fun isUIThread() = false

            override fun isBackgroundThread() = false

            override fun dispatchToBackground(block: RunBlock) {
                TODO("Not yet implemented")
            }

            override fun dispatchToUI(block: RunBlock) {
                TODO("Not yet implemented")
            }

            override fun assertIsUIThread() = Unit

            override fun assertIsBackgroundThread() = Unit
        }
    }

    fun provideMetricsDelegate(): MetricsDelegate = object : MetricsDelegate {
        override fun initialize() = Unit

        override fun log(tag: String, event: String) = Unit

        override fun log(tag: String, event: String, metadata: Map<String, String>) = Unit
    }

    fun provideMetricsInterface(
        metricsDelegate: MetricsDelegate,
        eventLoggerInterface: EventLoggerInterface,
    ): MetricsInterface {
        val instance = MetricsImpl(metricsDelegate, eventLoggerInterface)
        return Metrics.instance(instance)
    }

    fun provideThreadUtilInterface(threadUtilDelegate: ThreadUtilDelegate): ThreadUtilInterface {
        val instance = ThreadUtilImpl(threadUtilDelegate)
        return ThreadUtil.instance(instance)
    }

    fun provideClock(): Clock = Clock.System

    fun providePS2LinkRepository(
        dbgServiceClient: DBGServiceClient,
        clock: Clock,
    ): PS2LinkRepository = PS2LinkRepositoryImpl(dbgServiceClient, null, clock)

    fun provideDbgServiceClient(
        dbgCensus: DBGCensus,
        http: HttpClient,
        clock: Clock,
    ): DBGServiceClient = DBGServiceClientImpl(dbgCensus, http, clock)

    fun provideKtorHttpClient(json: Json): io.ktor.client.HttpClient {
        return buildHttpClient(json)
    }

    fun provideHttpClient(httpClient: io.ktor.client.HttpClient, json: Json): HttpClient {
        return HttpClient(httpClient, json)
    }

    fun provideServiceId(): String {
        return process.env.CENSUS_SERVICE_ID.unsafeCast<String?>() ?: ""
    }

    fun provideDbgCensus(serviceId: String): DBGCensus = DBGCensus(serviceId)

    fun provideBotToken(): String {
        return process.env.BOT_TOKEN.unsafeCast<String?>() ?: ""
    }
}
