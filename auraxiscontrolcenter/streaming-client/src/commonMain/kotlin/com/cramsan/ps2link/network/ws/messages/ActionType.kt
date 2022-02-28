package com.cramsan.ps2link.network.ws.messages

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Set the action to be executed in the [ClientCommand].
 *
 * @see Census API Docs - http://census.daybreakgames.com/]
 */
@Serializable
enum class ActionType {
    @SerialName("echo")
    ECHO,
    @SerialName("help")
    HELP,
    @SerialName("subscribe")
    SUBSCRIBE,
    @SerialName("clearSubscribe")
    CLEAR_SUBSCRIBE,
    @SerialName("recentCharacterIds")
    RECENT_CHARACTER_IDS,
    @SerialName("recentCharacterIdsCount")
    RECENT_CHARACTER_IDS_COUNT,
}
