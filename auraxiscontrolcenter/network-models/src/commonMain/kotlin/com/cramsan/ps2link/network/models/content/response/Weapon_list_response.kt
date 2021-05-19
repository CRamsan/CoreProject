package com.cramsan.ps2link.network.models.content.response

import com.cramsan.ps2link.network.models.content.item.Weapon
import kotlinx.serialization.Serializable

@Serializable
data class Weapon_list_response(
    var characters_weapon_stat_by_faction_list: List<Weapon>
)
