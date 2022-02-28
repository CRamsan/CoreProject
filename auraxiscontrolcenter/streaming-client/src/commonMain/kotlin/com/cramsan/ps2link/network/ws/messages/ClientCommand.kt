package com.cramsan.ps2link.network.ws.messages

import kotlinx.serialization.Serializable

/**
 * Base class for all commands that can be send to the API.
 *
 * @see Census API Docs - http://census.daybreakgames.com/]
 */
@Serializable
sealed class ClientCommand(
    val service: ServiceType,
    val action: ActionType,
)

/**
 * Command to request an overview about how to use the Census API.
 *
 * @see Census API Docs - http://census.daybreakgames.com/]
 */
@Serializable
class Help : ClientCommand(
    ServiceType.EVENT,
    ActionType.HELP,
)

/**
 * Echo command. Does not do anything.
 *
 * @see Census API Docs - http://census.daybreakgames.com/]
 */
@Serializable
class Echo : ClientCommand(
    ServiceType.EVENT,
    ActionType.ECHO,
)

/**
 * Request the API to subscribe to the events in [eventNames] for the characters with Ids in [characters].
 *
 * @see Census API Docs - http://census.daybreakgames.com/]
 */
@Serializable
data class CharacterSubscribe(
    val characters: List<String>,
    val eventNames: List<EventType>,
) : ClientCommand(
    ServiceType.EVENT,
    ActionType.SUBSCRIBE,
)

/**
 * Request the API to subscribe to the events in [eventNames] for the worlds with Ids in [worlds].
 *
 * @see Census API Docs - http://census.daybreakgames.com/]
 */
@Serializable
data class WorldSubscribe(
    val worlds: List<String>,
    val eventNames: List<EventType>,
) : ClientCommand(
    ServiceType.EVENT,
    ActionType.SUBSCRIBE,
)

/**
 * Request the API to clear the subscription for events in [eventNames] for the worlds with Ids in [worlds] and
 * character with Id in [characters].
 *
 * @see Census API Docs - http://census.daybreakgames.com/]
 */
@Serializable
data class ClearSubscribe(
    val characters: List<String>,
    val worlds: List<String>,
    val eventNames: List<EventType>,
) : ClientCommand(
    ServiceType.EVENT,
    ActionType.CLEAR_SUBSCRIBE,
)
