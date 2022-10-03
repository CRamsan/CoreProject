package me.cesar.application.spring

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import kotlinx.datetime.Instant

/**
 * Provide an [ObjectMapper] that is customized to support all the required serialization and deserialization
 * needs.
 *
 * @author cramsan
 */
fun createObjectMapper(): ObjectMapper {

    val kotlinModule = KotlinModule.Builder()
        .withReflectionCacheSize(CACHE_SIZE)
        .configure(KotlinFeature.NullToEmptyCollection, false)
        .configure(KotlinFeature.NullToEmptyMap, false)
        .configure(KotlinFeature.NullIsSameAsDefault, false)
        .configure(KotlinFeature.SingletonSupport, false)
        .configure(KotlinFeature.StrictNullChecks, false)
        .build()

    kotlinModule.addSerializer(KotlinInstantSerializer())
    kotlinModule.addDeserializer(Instant::class.java, KotlinInstantDeserializer())

    return ObjectMapper()
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
        .registerModule(kotlinModule)
}

private const val CACHE_SIZE = 512
