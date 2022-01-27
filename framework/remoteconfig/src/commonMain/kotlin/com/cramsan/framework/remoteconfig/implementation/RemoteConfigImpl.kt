package com.cramsan.framework.remoteconfig.implementation

import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.remoteconfig.RemoteConfig
import com.cramsan.framework.thread.ThreadUtilInterface
import io.ktor.client.HttpClient
import io.ktor.client.features.ResponseException
import io.ktor.client.request.get
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.Json

/**
 * Instance of [RemoteConfig] that fetches the remote config payload from the [remoteConfigEndpoint] with the
 * use of the [http] and [json]. If the payload cannot be retrieved, then [defaultConfigPayload] can be used
 * instead.
 */
@Suppress("LongParameterList")
class RemoteConfigImpl<T>(
    private val remoteConfigEndpoint: String,
    private val http: HttpClient,
    private val json: Json,
    private val defaultConfigPayload: T,
    private val deserializer: DeserializationStrategy<T>,
    private val threadUtil: ThreadUtilInterface,
    private val eventLogger: EventLoggerInterface,
    private val dispatcherProvider: DispatcherProvider,
    private val scope: CoroutineScope,
) : RemoteConfig<T> {

    private var downloadedConfigPayload: T? = null

    override fun isConfigPayloadReady(): Boolean {
        return downloadedConfigPayload != null
    }

    override suspend fun downloadConfigPayload(): Boolean {
        if (isConfigPayloadReady()) {
            eventLogger.d(TAG, "Config payload was already cached.")
            return true
        }
        threadUtil.assertIsBackgroundThread()

        return try {
            val body: String = http.get(remoteConfigEndpoint)
            eventLogger.d(TAG, "Remote config payload: $body")
            downloadedConfigPayload = json.decodeFromString(deserializer, body)
            eventLogger.i(TAG, "Config payload was downloaded successfully")
            isConfigPayloadReady()
        } catch (e: ResponseException) {
            eventLogger.w(TAG, "Exception when trying to make network call", e)
            false
        }
    }

    override fun downloadConfigPayloadAsync() {
        if (isConfigPayloadReady()) {
            eventLogger.d(TAG, "Config payload was already cached. Not starting async download.")
            return
        }

        scope.launch(dispatcherProvider.ioDispatcher()) {
            eventLogger.i(TAG, "Starting to download data async")
            downloadConfigPayload()
        }
    }

    override fun getConfigPayloadOrNull(): T? {
        return downloadedConfigPayload
    }

    override fun getConfigPayloadOrDefault(): T {
        return downloadedConfigPayload.let {
            if (it == null) {
                eventLogger.i(TAG, "getConfigPayloadOrDefault is returning default value")
                defaultConfigPayload
            } else {
                it
            }
        }
    }

    companion object {
        private const val TAG = "RemoteConfig"
    }
}
