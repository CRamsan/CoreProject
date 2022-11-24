package com.cramsan.ps2link.network.ws.testgui.di

import com.cramsan.ps2link.appcore.census.DBGCensus
import com.cramsan.ps2link.appcore.census.DBGServiceClient
import com.cramsan.ps2link.network.ws.StreamingClient
import com.cramsan.ps2link.network.ws.testgui.ApplicationModule
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json

/**
 * Initialize classes to be used by the application mid-layer, which comes from the PS2Link:appcore package. These
 * classes hold business logic to interface with the PS2 API and are not directly tied to the current application.
 */
class MiddleLayer(frameworkLayer: FrameworkLayer) {

    val backgroundDispatcher: CoroutineDispatcher

    val json: Json

    val serviceId: String

    val ktorHttpClient: HttpClient

    val httpClient: com.cramsan.ps2link.appcore.network.HttpClient

    val streamingClient: StreamingClient

    val clock: Clock

    val dbgCensus: DBGCensus

    val dbgClient: DBGServiceClient

    val applicationScope: CoroutineScope

    init {
        backgroundDispatcher = frameworkLayer.dispatcherProvider.ioDispatcher()

        json = ApplicationModule.provideJson()

        serviceId = ApplicationModule.provideServiceId()

        ktorHttpClient = ApplicationModule.provideKtorHttpClient(json)

        httpClient = ApplicationModule.provideHttpClient(
            ktorHttpClient,
            json,
        )

        streamingClient = ApplicationModule.provideStreamingClient(
            ktorHttpClient,
            json,
            serviceId,
            backgroundDispatcher,
        )

        clock = ApplicationModule.provideClock()

        dbgCensus = ApplicationModule.provideDbgCensus(serviceId)

        dbgClient = ApplicationModule.provideDbgServiceClient(
            dbgCensus,
            httpClient,
            clock,
        )

        applicationScope = ApplicationModule.providesApplicationCoroutineScope()
    }
}
