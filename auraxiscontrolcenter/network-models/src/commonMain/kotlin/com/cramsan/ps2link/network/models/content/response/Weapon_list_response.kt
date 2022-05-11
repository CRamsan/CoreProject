package com.cramsan.ps2link.network.models.content.response

import com.cramsan.ps2link.network.models.content.item.WeaponStat
import kotlinx.serialization.Serializable

@Serializable
data class Weapon_list_response(
    val characters_weapon_stat_by_faction_list: List<WeaponStat>,
)
