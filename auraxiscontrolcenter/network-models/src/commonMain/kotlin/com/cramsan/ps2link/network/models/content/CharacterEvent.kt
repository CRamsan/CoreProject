package com.cramsan.ps2link.network.models.content

import kotlinx.serialization.Serializable

@Serializable
data class CharacterEvent(
    var attacker: CharacterProfileLimited? = null,
    var character: CharacterProfileLimited? = null,
    var important_character_id: String? = null,
    var attacker_character_id: String? = null,
    var attacker_vehicle_id: String? = null,
    var attacker_weapon_id: String? = null,
    var character_id: String? = null,
    var is_critical: String? = null,
    var is_headshot: String? = null,
    var table_type: String? = null,
    var timestamp: String? = null,
    var world_id: String? = null,
    var zone_id: String? = null,
)
