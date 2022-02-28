package com.cramsan.ps2link.network.ws.messages

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The type of message that is sent from either client or API.
 *
 * @see Census API Docs - http://census.daybreakgames.com/]
 */
@Serializable
enum class ServiceType {
    @SerialName("event")
    EVENT,
    @SerialName("push")
    PUSH,
}
