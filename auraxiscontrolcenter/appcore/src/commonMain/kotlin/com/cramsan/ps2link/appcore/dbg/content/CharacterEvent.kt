package com.cramsan.ps2link.appcore.dbg.content

data class CharacterEvent (
    var attacker: CharacterProfileLimited? = null,
    var character: CharacterProfileLimited? = null,
    var important_character_id: String? = null,
    var attacker_character_id: String? = null,
    var attacker_vehicle_id: String? = null,
    var attacker_weapon_id: String? = null,
    var character_id: String? = null,
    var is_critical: String? = null,
    var is_headshot: String? = null,
    var weapon_name: String? = null,
    var table_type: String? = null,
    var imagePath: String? = null,
    var timestamp: String? = null,
    var world_id: String? = null,
    var zone_id: String? = null,
)