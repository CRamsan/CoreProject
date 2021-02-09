package com.cramsan.ps2link.appcore.network

import com.cramsan.framework.logging.logD
import com.cramsan.framework.metrics.logMetric
import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Url
import io.ktor.http.isSuccess
import kotlinx.coroutines.delay
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
) {

    @OptIn(ExperimentalTime::class)
    suspend inline fun <reified T> sendRequestWithRetry(url: Url): T? {
        for (retry in 0..3) {
            delay(retry.toDuration(DurationUnit.SECONDS))
            val response = sendRequest(url, retry)

            return if (response.status.isSuccess()) {
                response.receive()
            } else if (response.status.value in 300..500) {
                null
            } else {
                continue
            }
        }

        return null
    }

    @OptIn(ExperimentalTime::class)
    suspend fun sendRequest(url: Url, retry: Int): HttpResponse {
        logD(TAG, "Url: $url")

        val response: HttpResponse
        val latency = measureTime {
            response = http.get(url)
        }

        val metadata = mapOf(
            RESPONSE_CODE to response.status.value,
            LATENCY to latency,
            RETRY to retry,
        ).mapValues { it.value.toString() }
        logMetric(TAG, "Request Completed", metadata)

        return response
    }

    companion object {
        val TAG = "HttpClient"

        val RESPONSE_CODE = "RESPONSE_CODE"
        val LATENCY = "LATENCY"
        val RETRY = "RETRY"
    }
}