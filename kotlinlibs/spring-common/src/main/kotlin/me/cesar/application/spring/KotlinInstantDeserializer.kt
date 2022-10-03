package me.cesar.application.spring

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import kotlinx.datetime.Instant

/**
 * Deserializer that is used to deserialize a [Long] into an instance of [Instant].
 */
class KotlinInstantDeserializer : StdDeserializer<Instant>(Instant::class.java) {
    override fun deserialize(
        p: JsonParser,
        ctxt: DeserializationContext?,
    ): Instant {
        return Instant.fromEpochSeconds(p.longValue)
    }
}
