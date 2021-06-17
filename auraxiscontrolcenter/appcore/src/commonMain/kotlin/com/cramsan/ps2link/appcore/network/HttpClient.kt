package com.cramsan.ps2link.appcore.network

import com.cramsan.framework.logging.logD
import com.cramsan.framework.logging.logW
import com.cramsan.framework.metrics.logMetric
import com.cramsan.ps2link.appcore.census.UrlHolder
import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.coroutines.delay
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
) {

    @OptIn(ExperimentalTime::class)
    suspend inline fun <reified T> sendRequestWithRetry(url: UrlHolder): com.cramsan.ps2link.appcore.network.PS2HttpResponse<T> {
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
        logD(TAG, "Url: ${url.completeUrl}")

        val response: HttpResponse
        val latency = measureTime {
            response = http.get(url.completeUrl)
        }

        val normalizedLatency = (latency.toLong(DurationUnit.MILLISECONDS) / 200L) * 200

        val metadata = mapOf(
            URL_ID to url.urlIdentifier,
            RESPONSE_CODE to response.status.value,
            LATENCY to normalizedLatency,
            RETRY to retry,
        ).mapValues { it.value.toString() }
        logMetric(TAG, "Request Completed", metadata)

        return response
    }

    companion object {
        val TAG = "HttpClient"

        val RESPONSE_CODE = "RESPONSE_CODE"
        val URL_ID = "URL_ID"
        val LATENCY = "LATENCY"
        val RETRY = "RETRY"
    }
}
