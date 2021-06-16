package com.cramsan.ps2link.network.models.content

import kotlinx.serialization.Serializable

@Serializable
data class CharacterEvent(
    val attacker: CharacterProfileLimited? = null,
    val character: CharacterProfileLimited? = null,
    val important_character_id: String? = null,
    val attacker_character_id: String? = null,
    val attacker_vehicle_id: String? = null,
    val attacker_weapon_id: String? = null,
    val character_id: String? = null,
    val is_critical: String? = null,
    val is_headshot: String? = null,
    val table_type: String? = null,
    val timestamp: String? = null,
    val world_id: String? = null,
    val zone_id: String? = null,
)
