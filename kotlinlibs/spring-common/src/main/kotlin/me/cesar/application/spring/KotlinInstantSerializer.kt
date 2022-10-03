package me.cesar.application.spring

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import kotlinx.datetime.Instant

/**
 * Serializer that is used to serialize an instance of [Instant] as a [Long].
 */
class KotlinInstantSerializer : StdSerializer<Instant>(Instant::class.java) {
    override fun serialize(
        value: Instant,
        jgen: JsonGenerator,
        provider: SerializerProvider?,
    ) {
        jgen.writeNumber(value.epochSeconds)
    }
}
