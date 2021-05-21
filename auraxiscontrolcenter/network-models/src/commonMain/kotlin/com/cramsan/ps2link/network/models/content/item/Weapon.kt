package com.cramsan.ps2link.network.models.content.item

import com.cramsan.ps2link.network.models.content.world.Name_Multi
import kotlinx.serialization.Serializable

@Serializable
data class Weapon(
    var weapon_id: String? = null,
    var turn_modifier: String? = null,
    var move_modifier: String? = null,
    var sprint_recovery_ms: String? = null,
    val equip_ms: Name_Multi? = null,
    val unequip_ms: Description? = null,
    var to_iron_sights_ms: String? = null,
    var from_iron_sights_ms: String? = null,
    var melee_detect_width: String? = null,
    var melee_detect_height: String? = null,
)
