package com.cramsan.ps2link.network.ws.messages

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Base class for all payloads received in a [ServiceMessage].
 *
 * @see Census API Docs - http://census.daybreakgames.com/]
 */
@Serializable
sealed class ServerEventPayload

/**
 * V2 Events from the WebSocket API. These events are sent by our implementation of a WS server.
 */
sealed class ServerEventPayloadV2 : ServerEventPayload() {

    /**
     * V2 death event. This event contains some improvements like [attackerCharacterName],
     * [attackerCharacterRank], [attackerWeaponName] and [attackerWeaponImageUrl].
     */
    @Serializable
    data class DeathV2(
        val attackerCharacterId: String?,
        val attackerCharacterName: String?,
        val attackerCharacterFaction: String?,
        val attackerCharacterRank: Int?,
        val attackerFireModeId: String?,
        val attackerLoadoutId: String?,
        val attackerVehicleId: String?,
        val attackerWeaponId: String?,
        val attackerWeaponName: String?,
        val attackerWeaponImageUrl: String?,
        val characterId: String?,
        val characterName: String?,
        val characterFaction: String?,
        val characterRank: Int?,
        val characterLoadoutId: String?,
        val isCritical: String?,
        val isHeadshot: String?,
        val timestamp: String?,
        val vehicleId: String?,
        val worldId: String?,
        val zoneId: String?,
        @SerialName("event_name")
        val eventName: EventName = EventName.DEATH,
    ) : ServerEventPayload()
}

/**
 * The continent was locked. This is a world event.
 *
 * @see Census API Docs - http://census.daybreakgames.com/]
 */

@Serializable
data class ContinentLock(
    @SerialName("event_name")
    val eventName: EventName,
    @SerialName("timestamp")
    val timestamp: String? = null,
    @SerialName("world_id")
    val worldId: String? = null,
    @SerialName("zone_id")
    val zoneId: String? = null,
    @SerialName("triggering_faction")
    val triggeringFaction: String? = null,
    @SerialName("previous_faction")
    val previousFaction: String? = null,
    @SerialName("vs_population")
    val VSPopulation: String? = null,
    @SerialName("nc_population")
    val NCPopulation: String? = null,
    @SerialName("tr_population")
    val TRPopulation: String? = null,
    @SerialName("metagame_event_id")
    val metagameEventId: String? = null,
    @SerialName("event_type")
    val eventType: String?,
) : ServerEventPayload()

/**
 * The continent was unlocked. This is a world event.
 *
 * @see Census API Docs - http://census.daybreakgames.com/]
 */
@Serializable
data class ContinentUnlock(
    @SerialName("event_name")
    val eventName: EventName,
    @SerialName("timestamp")
    val timestamp: String? = null,
    @SerialName("world_id")
    val worldId: String? = null,
    @SerialName("zone_id")
    val zoneId: String? = null,
    @SerialName("triggering_faction")
    val triggeringFaction: String? = null,
    @SerialName("previous_faction")
    val previousFaction: String? = null,
    @SerialName("vs_population")
    val VSPopulation: String? = null,
    @SerialName("nc_population")
    val NCPopulation: String? = null,
    @SerialName("tr_population")
    val TRPopulation: String? = null,
    @SerialName("metagame_event_id")
    val metagameEventId: String? = null,
    @SerialName("event_type")
    val eventType: String?,
) : ServerEventPayload()

/**
 * A territory changed in ownership. This is a world-based event. For character
 * based events, look at [PlayerFacilityCapture] or [PlayerFacilityDefend].
 *
 * @see Census API Docs - http://census.daybreakgames.com/]
 */
@Serializable
data class FacilityControl(
    @SerialName("event_name")
    val eventName: EventName,
    @SerialName("timestamp")
    val timestamp: String? = null,
    @SerialName("world_id")
    val worldId: String? = null,
    @SerialName("old_faction_id")
    val oldFactionId: String? = null,
    @SerialName("outfit_id")
    val outfitId: String? = null,
    @SerialName("new_faction_id")
    val newFactionId: String? = null,
    @SerialName("facility_id")
    val facilityId: String? = null,
    @SerialName("duration_held")
    val durationHeld: String? = null,
    @SerialName("zone_id")
    val zoneId: String? = null,
) : ServerEventPayload()

/**
 * Alerts or other type of metagame events. This is a world event.
 *
 * @see Census API Docs - http://census.daybreakgames.com/]
 */
@Serializable
data class MetagameEvent(
    @SerialName("event_name")
    val eventName: EventName,
    @SerialName("timestamp")
    val timestamp: String? = null,
    @SerialName("world_id")
    val worldId: String? = null,
    @SerialName("experience_bonus")
    val experienceBonus: String? = null,
    @SerialName("faction_nc")
    val factionNC: String? = null,
    @SerialName("faction_tr")
    val factionTR: String? = null,
    @SerialName("faction_vs")
    val factionVS: String? = null,
    @SerialName("metagame_event_id")
    val metagameEventId: String? = null,
    @SerialName("metagame_event_state")
    val metagameEventState: String? = null,
    @SerialName("zone_id")
    val zoneId: String? = null,
) : ServerEventPayload()

/**
 * Character received an achievement. This is a character event.
 *
 * @see Census API Docs - http://census.daybreakgames.com/]
 */
@Serializable
data class AchievementEarned(
    @SerialName("event_name")
    val eventName: EventName,
    @SerialName("timestamp")
    val timestamp: String? = null,
    @SerialName("world_id")
    val worldId: String? = null,
    @SerialName("zone_id")
    val zoneId: String? = null,
    @SerialName("achievement_id")
    val achievementId: String? = null,
) : ServerEventPayload()

/**
 * Character leveled up. This is a character event.
 *
 * @see Census API Docs - http://census.daybreakgames.com/]
 */
@Serializable
data class BattleRankUp(
    @SerialName("event_name")
    val eventName: EventName,
    @SerialName("timestamp")
    val timestamp: String? = null,
    @SerialName("world_id")
    val worldId: String? = null,
    @SerialName("zone_id")
    val zoneId: String? = null,
    @SerialName("battle_rank")
    val battleRank: String? = null,
    @SerialName("character_id")
    val characterId: String? = null,
) : ServerEventPayload()

/**
 * A character died. This is a character event.
 *
 * @see Census API Docs - http://census.daybreakgames.com/]
 */
@Serializable
data class Death(
    @SerialName("attacker_character_id")
    val attackerCharacterId: String? = null,
    @SerialName("attacker_fire_mode_id")
    val attackerFireModeId: String? = null,
    @SerialName("attacker_loadout_id")
    val attackerLoadoutId: String? = null,
    @SerialName("attacker_vehicle_id")
    val attackerVehicleId: String? = null,
    @SerialName("attacker_weapon_id")
    val attackerWeaponId: String? = null,
    @SerialName("character_id")
    val characterId: String? = null,
    @SerialName("character_loadout_id")
    val characterLoadoutId: String? = null,
    @SerialName("event_name")
    val eventName: EventName,
    @SerialName("is_critical")
    val isCritical: String? = null,
    @SerialName("is_headshot")
    val isHeadshot: String? = null,
    @SerialName("timestamp")
    val timestamp: String? = null,
    @SerialName("vehicle_id")
    val vehicleId: String? = null,
    @SerialName("world_id")
    val worldId: String? = null,
    @SerialName("zone_id")
    val zoneId: String? = null,
) : ServerEventPayload()

/**
 * A character gained experience. This is a character event.
 *
 * Match experience_id with http://census.daybreakgames.com/get/ps2/experience to find out what they did.
 * Field other_id may refer to another player, or a vehicle, etc. You can filter GainExperience events by
 * experience_id. Details here.
 *
 * @see Census API Docs - http://census.daybreakgames.com/]
 */
@Serializable
data class GainExperience(
    @SerialName("amount")
    val amount: String? = null,
    @SerialName("character_id")
    val characterId: String? = null,
    @SerialName("event_name")
    val eventName: EventName,
    @SerialName("experience_id")
    val experienceId: String? = null,
    @SerialName("loadout_id")
    val loadoutId: String? = null,
    @SerialName("other_id")
    val otherId: String? = null,
    @SerialName("timestamp")
    val timestamp: String? = null,
    @SerialName("world_id")
    val worldId: String? = null,
    @SerialName("zone_id")
    val zoneId: String? = null,
) : ServerEventPayload()

/**
 * A player unlocked a new item. This is a character event.
 *
 * @see Census API Docs - http://census.daybreakgames.com/]
 */
@Serializable
data class ItemAdded(
    @SerialName("character_id")
    val characterId: String? = null,
    @SerialName("context")
    val context: String? = null,
    @SerialName("event_name")
    val eventName: EventName,
    @SerialName("item_count")
    val itemCount: String? = null,
    @SerialName("item_id")
    val itemId: String? = null,
    @SerialName("timestamp")
    val timestamp: String? = null,
    @SerialName("world_id")
    val worldId: String? = null,
    @SerialName("zone_id")
    val zoneId: String? = null,
) : ServerEventPayload()

/**
 * A player captured a territory. This is a character event.
 *
 * @see Census API Docs - http://census.daybreakgames.com/]
 */
@Serializable
data class PlayerFacilityCapture(
    @SerialName("character_id")
    val characterId: String? = null,
    @SerialName("event_name")
    val eventName: EventName,
    @SerialName("facility_id")
    val facilityId: String? = null,
    @SerialName("outfit_id")
    val outfitId: String? = null,
    @SerialName("timestamp")
    val timestamp: String? = null,
    @SerialName("world_id")
    val worldId: String? = null,
    @SerialName("zone_id")
    val zoneId: String? = null,
) : ServerEventPayload()

/**
 * A player defended a territory. This is a character event.
 *
 * @see Census API Docs - http://census.daybreakgames.com/]
 */
@Serializable
data class PlayerFacilityDefend(
    @SerialName("character_id")
    val characterId: String? = null,
    @SerialName("event_name")
    val eventName: EventName,
    @SerialName("facility_id")
    val facilityId: String? = null,
    @SerialName("outfit_id")
    val outfitId: String? = null,
    @SerialName("timestamp")
    val timestamp: String? = null,
    @SerialName("world_id")
    val worldId: String? = null,
    @SerialName("zone_id")
    val zoneId: String? = null,
) : ServerEventPayload()

/**
 * A player has unlocked a new skill/certification. This is a character event.
 *
 * @see Census API Docs - http://census.daybreakgames.com/]
 */
@Serializable
data class SkillAdded(
    @SerialName("event_name")
    val eventName: EventName,
    @SerialName("timestamp")
    val timestamp: String? = null,
    @SerialName("world_id")
    val zoneId: String? = null,
    @SerialName("character_id")
    val characterId: String? = null,
    @SerialName("skill_id")
    val skillId: String? = null,
) : ServerEventPayload()

/**
 * A vehicle has been destroyed. This is a character event.
 *
 * @see Census API Docs - http://census.daybreakgames.com/]
 */
@Serializable
data class VehicleDestroy(
    @SerialName("attacker_character_id")
    val attackerCharacterId: String? = null,
    @SerialName("attacker_loadout_id")
    val attackerLoadoutId: String? = null,
    @SerialName("attacker_vehicle_id")
    val attackerVehicleId: String? = null,
    @SerialName("attacker_weapon_id")
    val attackerWeaponId: String? = null,
    @SerialName("character_id")
    val characterId: String? = null,
    @SerialName("event_name")
    val eventName: String? = null,
    @SerialName("facility_id")
    val facilityId: String? = null,
    @SerialName("faction_id")
    val factionId: String? = null,
    @SerialName("timestamp")
    val timestamp: String? = null,
    @SerialName("vehicle_id")
    val vehicleId: String? = null,
    @SerialName("world_id")
    val worldId: String? = null,
    @SerialName("zone_id")
    val zoneId: String? = null,
) : ServerEventPayload()

/**
 * A player logged in. This is a character and world event.
 *
 * @see Census API Docs - http://census.daybreakgames.com/]
 */
@Serializable
data class PlayerLogin(
    @SerialName("event_name")
    val eventName: EventName,
    @SerialName("timestamp")
    val timestamp: String? = null,
    @SerialName("world_id")
    val zoneId: String? = null,
    @SerialName("character_id")
    val characterId: String? = null,
) : ServerEventPayload()

/**
 * A player logged out. This is a character and world event.
 *
 * @see Census API Docs - http://census.daybreakgames.com/]
 */
@Serializable
data class PlayerLogout(
    @SerialName("event_name")
    val eventName: EventName,
    @SerialName("timestamp")
    val timestamp: String? = null,
    @SerialName("world_id")
    val zoneId: String? = null,
    @SerialName("character_id")
    val characterId: String? = null,
) : ServerEventPayload()
