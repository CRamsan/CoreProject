package com.cramsan.stranded.server.messages

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Serialize the [event] into a JSON string.
 */
fun Json.createSerializedMessage(event: ServerEvent) = encodeToString(event)

/**
 * Parse the provided [textFrame] into a [ClientEvent].
 */
fun Json.parseClientEvent(
    textFrame: String,
): ClientEvent {
    return decodeFromString(textFrame)
}

/**
 * Serialize the [event] into a JSON string.
 */
fun Json.createSerializedClientMessage(event: ClientEvent) = encodeToString(event)

/**
 * Parse the provided [textFrame] into a [ServerEvent].
 */
fun Json.parseServerEvent(textFrame: String): ServerEvent {
    return decodeFromString(textFrame)
}
