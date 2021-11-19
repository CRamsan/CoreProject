package com.cramsan.stranded.lib.messages

import com.cramsan.stranded.lib.game.logic.GameState
import com.cramsan.stranded.lib.game.logic.MutableGameState
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

val module = SerializersModule {
    polymorphic(GameState::class) {
        subclass(MutableGameState::class)
    }
}
private val serializer = Json {
    serializersModule = module
}

/**
 * Serialize the [event] into a JSON string.
 */
fun createSerializedMessage(event: ServerEvent) = serializer.encodeToString(event)

/**
 * Parse the provided [textFrame] into a [ClientEvent].
 */
fun parseClientEvent(
    textFrame: String,
): ClientEvent {
    return serializer.decodeFromString(textFrame)
}

/**
 * Serialize the [event] into a JSON string.
 */
fun createSerializedClientMessage(event: ClientEvent) = serializer.encodeToString(event)

/**
 * Parse the provided [textFrame] into a [ServerEvent].
 */
fun parseServerEvent(textFrame: String): ServerEvent {
    return serializer.decodeFromString(textFrame)
}
