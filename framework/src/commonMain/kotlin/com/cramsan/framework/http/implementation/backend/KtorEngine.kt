package com.cramsan.framework.http.implementation.backend

import com.cramsan.framework.http.HttpEngine
import com.cramsan.framework.logging.EventLoggerInterface
import io.ktor.client.HttpClient
import io.ktor.client.call.Type
import io.ktor.client.call.TypeInfo
import io.ktor.client.call.call
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.defaultSerializer
import io.ktor.client.request.HttpRequestBuilder
import kotlin.reflect.KClass

class KtorEngine(
    private val eventLogger: EventLoggerInterface
) : HttpEngine {

    private class InvalidReifiedType : Type

    private val reifiedType = InvalidReifiedType()

    private val client = HttpClient {
        install(JsonFeature) {
            serializer = defaultSerializer()
        }
    }

    override fun <T> get(url: String, callback: T) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun <T> getBlocking(url: String): T {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun <T : Any> get(url: String, c: KClass<T>): T {
        val builder = HttpRequestBuilder()
        val clientCall = this.client.call(builder)
        val typeInfo = TypeInfo(c, reifiedType)
        return clientCall.receive(typeInfo) as T
    }
}
