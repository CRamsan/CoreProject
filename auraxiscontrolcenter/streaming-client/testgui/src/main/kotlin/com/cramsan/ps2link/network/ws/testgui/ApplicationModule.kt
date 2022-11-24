package com.cramsan.ps2link.network.ws.testgui

import com.cramsan.framework.assertlib.AssertUtil
import com.cramsan.framework.assertlib.AssertUtilInterface
import com.cramsan.framework.assertlib.implementation.AssertUtilImpl
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.core.JVMDispatcherProvider
import com.cramsan.framework.halt.HaltUtil
import com.cramsan.framework.halt.HaltUtilDelegate
import com.cramsan.framework.halt.implementation.HaltUtilImpl
import com.cramsan.framework.halt.implementation.HaltUtilJVM
import com.cramsan.framework.logging.EventLogger
import com.cramsan.framework.logging.EventLoggerDelegate
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.implementation.EventLoggerImpl
import com.cramsan.framework.logging.implementation.LoggerJVM
import com.cramsan.framework.preferences.Preferences
import com.cramsan.framework.preferences.PreferencesDelegate
import com.cramsan.framework.preferences.implementation.JVMPreferencesDelegate
import com.cramsan.framework.preferences.implementation.PreferencesImpl
import com.cramsan.framework.thread.ThreadUtil
import com.cramsan.framework.thread.ThreadUtilDelegate
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.framework.thread.implementation.ThreadUtilImpl
import com.cramsan.framework.thread.implementation.ThreadUtilJVM
import com.cramsan.ps2link.appcore.census.DBGCensus
import com.cramsan.ps2link.appcore.census.DBGServiceClient
import com.cramsan.ps2link.appcore.census.DBGServiceClientImpl
import com.cramsan.ps2link.appcore.census.buildHttpClient
import com.cramsan.ps2link.appcore.network.HttpClient
import com.cramsan.ps2link.network.ws.Environment
import com.cramsan.ps2link.network.ws.StreamingClient
import io.ktor.client.plugins.websocket.WebSockets
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import org.ocpsoft.prettytime.PrettyTime

/**
 * This class provides constructor for all dependencies in the application. This class is used as a placeholder until
 * we transition to a real DI framework.
 */
@Suppress("UndocumentedPublicFunction", "TooManyFunctions")
object ApplicationModule {

    fun provideAssertUtil(
        eventLoggerInterface: EventLoggerInterface,
        haltUtilInterface: HaltUtil,
    ): AssertUtilInterface {
        val impl = AssertUtilImpl(
            true,
            eventLoggerInterface,
            haltUtilInterface,
        )
        AssertUtil.setInstance(impl)
        return AssertUtil.singleton
    }

    fun provideThreadUtilDelegate(
        eventLogger: EventLoggerInterface,
        assertUtilInterface: AssertUtilInterface,
    ): ThreadUtilDelegate {
        return ThreadUtilJVM(eventLogger, assertUtilInterface)
    }

    fun provideEventLoggerDelegate(): EventLoggerDelegate = LoggerJVM()

    fun provideEventLoggerInterface(
        eventLoggerDelegate: EventLoggerDelegate,
    ): EventLoggerInterface {
        val severity: Severity = Severity.DEBUG
        val instance = EventLoggerImpl(severity, null, eventLoggerDelegate)
        EventLogger.setInstance(instance)
        return EventLogger.singleton
    }

    fun provideHaltUtilDelegate(): HaltUtilDelegate = HaltUtilJVM()

    fun provideHaltUtilInterface(haltUtilDelegate: HaltUtilDelegate): HaltUtil =
        HaltUtilImpl(haltUtilDelegate)

    fun provideThreadUtilInterface(threadUtilDelegate: ThreadUtilDelegate): ThreadUtilInterface {
        val instance = ThreadUtilImpl(threadUtilDelegate)
        ThreadUtil.setInstance(instance)
        return ThreadUtil.singleton
    }

    fun providePreferencesDelegate(): PreferencesDelegate = JVMPreferencesDelegate()

    fun providePreferencesInterface(preferencesDelegate: PreferencesDelegate): Preferences =
        PreferencesImpl(preferencesDelegate)

    fun provideDispatcher(): DispatcherProvider = JVMDispatcherProvider()

    fun providesApplicationCoroutineScope(): CoroutineScope {
        // Run this code when providing an instance of CoroutineScope
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    fun provideClock(): Clock = Clock.System

    fun providePrettyTime(): PrettyTime = PrettyTime()

    fun provideJson() = Json {
        prettyPrint = false
        ignoreUnknownKeys = true
        serializersModule = SerializersModule {
            classDiscriminator = "_type"
        }
    }

    fun provideDbgServiceClient(
        dbgCensus: DBGCensus,
        http: HttpClient,
        clock: Clock,
    ): DBGServiceClient = DBGServiceClientImpl(dbgCensus, http, clock)

    fun provideKtorHttpClient(json: Json): io.ktor.client.HttpClient {
        return buildHttpClient(json).config {
            install(WebSockets)
        }
    }

    fun provideStreamingClient(
        ktorHttpClient: io.ktor.client.HttpClient,
        json: Json,
        serviceId: String,
        dispatcher: CoroutineDispatcher,
    ): StreamingClient {
        return StreamingClient(
            ktorHttpClient,
            json,
            serviceId,
            Environment.PS2,
            dispatcher,
        )
    }

    fun provideHttpClient(
        httpClient: io.ktor.client.HttpClient,
        json: Json,
    ): HttpClient {
        return HttpClient(httpClient, json, null, null)
    }

    fun provideDbgCensus(serviceId: String): DBGCensus = DBGCensus(serviceId)

    fun provideServiceId(): String = "PS2LinkCompanion"
}
