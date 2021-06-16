package com.cramsan.ps2link.network.models.content.item

import com.cramsan.ps2link.network.models.content.world.Name_Multi
import kotlinx.serialization.Serializable

@Serializable
data class Weapon(
    val weapon_id: String? = null,
    val turn_modifier: String? = null,
    val move_modifier: String? = null,
    val sprint_recovery_ms: String? = null,
    val equip_ms: Name_Multi? = null,
    val unequip_ms: Description? = null,
    val to_iron_sights_ms: String? = null,
    val from_iron_sights_ms: String? = null,
    val melee_detect_width: String? = null,
    val melee_detect_height: String? = null,
)
