package com.cramsan.ps2link.network.ws.messages

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Type of the message event received. This is used for [ServerEvent] instances.
 *
 * @see Census API Docs - http://census.daybreakgames.com/]
 */
@Serializable
enum class ServerMessageType {
    @SerialName("connectionStateChanged")
    CONNECTION_STATE_CHANGED,
    @SerialName("heartbeat")
    HEARTBEAT,
    @SerialName("serviceStateChanged")
    SERVICE_STATE_CHANGED,
    @SerialName("serviceMessage")
    SERVICE_MESSAGE,
    ;
}
