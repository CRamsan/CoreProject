package com.cramsan.ps2link.network.ws.messages

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Define the type of events that can be subscribed/unsubscribed in [CharacterSubscribe], [WorldSubscribe] or
 * [ClearSubscribe].
 *
 * @see Census API Docs - http://census.daybreakgames.com/]
 */

@Serializable
enum class EventType {
    @SerialName("all")
    ALL,
    @SerialName("AchievementEarned")
    ACHIEVEMENT_EARNED,
    @SerialName("BattleRankUp")
    BATTLE_RANK_UP,
    @SerialName("Death")
    DEATH,
    @SerialName("ItemAdded")
    ITEM_ADDED,
    @SerialName("SkillAdded")
    SKILL_ADDED,
    @SerialName("VehicleDestroy")
    VEHICLE_DESTROY,
    @SerialName("GainExperience")
    GAIN_EXPERIENCE,
    @SerialName("PlayerFacilityCapture")
    PLAYER_FACILITY_CAPTURE,
    @SerialName("PlayerFacilityDefend")
    PLAYER_FACILITY_DEFEND,
    @SerialName("ContinentLock")
    CONTINENT_LOCK,
    @SerialName("ContinentUnlock")
    CONTINENT_UNLOCK,
    @SerialName("FacilityControl")
    FACILITY_CONTROL,
    @SerialName("MetagameEvent")
    METAGAME_EVENT,
    @SerialName("PlayerLogin")
    PLAYER_LOGIN,
    @SerialName("PlayerLogout")
    PLAYER_LOGOUT,
}
