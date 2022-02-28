package com.cramsan.ps2link.network.ws.messages

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Set the type of payload that was received from the API. This enum is used for [ServerEventPayload] and it is only
 * for messages of type [ServiceMessage].
 *
 * @see Census API Docs - http://census.daybreakgames.com/]
 */
@Serializable
enum class EventName {
    @SerialName("ContinentLock")
    CONTINENT_LOCK,
    @SerialName("ContinentUnlock")
    CONTINENT_UNLOCK,
    @SerialName("FacilityControl")
    FACILITY_CONTROL,
    @SerialName("MetagameEvent")
    METAGAME_EVENT,
    @SerialName("AchievementEarned")
    ACHIEVEMENT_EARNED,
    @SerialName("BattleRankUp")
    BATTLE_RANK_UP,
    @SerialName("Death")
    DEATH,
    @SerialName("GainExperience")
    GAIN_EXPERIENCE,
    @SerialName("ItemAdded")
    ITEM_ADDED,
    @SerialName("PlayerFacilityCapture")
    PLAYER_FACILITY_CAPTURE,
    @SerialName("PlayerFacilityDefend")
    PLAYER_FACILITY_DEFEND,
    @SerialName("PlayerLogin")
    PLAYER_LOGIN,
    @SerialName("PlayerLogout")
    PLAYER_LOGOUT,
    @SerialName("SkillAdded")
    SKILL_ADDED,
    @SerialName("VehicleDestroy")
    VEHICLE_DESTROY,
}
