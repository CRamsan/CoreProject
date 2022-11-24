package com.cramsan.ps2link.network.ws.messages

import kotlinx.serialization.Serializable

/**
 * Base class for all commands that can be received from the API.
 *
 * @see Census API Docs - http://census.daybreakgames.com/]
 */
@Serializable
sealed class ServerEvent

/**
 * Event about a change in the connection from this client to the API.
 *
 * @see Census API Docs - http://census.daybreakgames.com/]
 */
@Serializable
data class ConnectionStateChanged(
    val connected: String?,
    val service: ServiceType?,
    val type: ServerMessageType?,
) : ServerEvent()

/**
 * Changes to the internal state of the service. You can ignore this event.
 *
 * @see Census API Docs - http://census.daybreakgames.com/]
 */
@Serializable
data class ServiceStateChanged(
    val detail: String?,
    val online: String?,
    val service: ServiceType?,
    val type: ServerMessageType?,
) : ServerEvent()

/**
 * Event send by the API to keep the connection alive. You can ignore this event.
 *
 * @see Census API Docs - http://census.daybreakgames.com/]
 */
@Serializable
data class Heartbeat(
    val online: Map<String, String>?,
    val service: ServiceType?,
    val type: ServerMessageType?,
) : ServerEvent()

/**
 * Event for the [EventName] that you are subscribed to. The [payload] will be of type [ServerEventPayload].
 *
 * @see Census API Docs - http://census.daybreakgames.com/]
 */
@Serializable
data class ServiceMessage<T : ServerEventPayload>(
    val service: ServiceType?,
    val type: ServerMessageType?,
    val payload: T?,
) : ServerEvent()

/**
 * Server event that confirms that a subscription was made.
 */
@Serializable
data class SubscriptionConfirmation(
    val subscription: SubscriptionResponse?,
) : ServerEvent() {

    /**
     * Class that holds the information regarding the current subscription.
     */
    @Serializable
    data class SubscriptionResponse(
        val characterCount: Int?,
        val eventNames: List<String>?,
        val logicalAndCharactersWithWorlds: Boolean?,
        val worlds: List<String>,
    )
}

/**
 * Event that represents that [rawMessage] could not be mapped to any of the know types of events.
 */
@Serializable
data class UnhandledEvent(
    val rawMessage: String,
) : ServerEvent()
