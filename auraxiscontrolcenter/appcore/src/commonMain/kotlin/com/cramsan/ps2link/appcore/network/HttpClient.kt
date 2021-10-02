package com.cramsan.ps2link.appcore.network

import com.cramsan.framework.logging.logD
import com.cramsan.framework.logging.logW
import com.cramsan.framework.metrics.MetricType
import com.cramsan.framework.metrics.MetricUnit
import com.cramsan.framework.metrics.MetricsInterface
import com.cramsan.ps2link.appcore.census.UrlHolder
import com.cramsan.ps2link.metric.HttpNamespace
import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.coroutines.delay
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime
import kotlin.time.toDuration

/**
 * @Author cramsan
 * @created 2/8/2021
 */
class HttpClient(
    private val http: HttpClient,
    val json: Json,
    private val metrics: MetricsInterface,
) {

    @OptIn(ExperimentalSerializationApi::class, ExperimentalTime::class)
    suspend inline fun <reified T> sendRequestWithRetry(url: UrlHolder): PS2HttpResponse<T> {
        for (retry in 0..3) {
            delay(retry.toDuration(DurationUnit.SECONDS))
            return try {
                val response = sendRequest(url, retry)

                if (response.status.isSuccess()) {
                    val body = response.receive<String>()
                    logD(TAG, "Response: $body")
                    val parsedBody = json.decodeFromString<T>(body)
                    PS2HttpResponse.success(parsedBody, response)
                } else if (response.status.value in 300..500) {
                    PS2HttpResponse.failure(response, null)
                } else {
                    continue
                }
            } catch (exception: Exception) {
                logW(TAG, "Unexpected Exception", exception)
                PS2HttpResponse.failure(null, exception)
            } catch (throwable: Throwable) {
                logW(TAG, "Unexpected Throwable", throwable)
                PS2HttpResponse.failure(null, throwable)
            }
        }
        return PS2HttpResponse.failure(null, null)
    }

    @OptIn(ExperimentalTime::class)
    suspend fun sendRequest(url: UrlHolder, retry: Int): HttpResponse {
        logD(TAG, "Url: ${url.completeUrl} - retry: $retry")

        val response: HttpResponse
        val latency = measureTime {
            response = http.get(url.completeUrl)
        }

        if (response.status.isSuccess()) {
            metrics.record(MetricType.SUCCESS, HttpNamespace, url.urlIdentifier.name)
        } else {
            metrics.record(MetricType.FAILURE, HttpNamespace, url.urlIdentifier.name)
        }
        metrics.record(
            MetricType.LATENCY,
            HttpNamespace,
            url.urlIdentifier.name,
            value = latency.toDouble(DurationUnit.MILLISECONDS),
            unit = MetricUnit.MILLIS
        )

        return response
    }

    companion object {
        const val TAG = "HttpClient"
    }
}
