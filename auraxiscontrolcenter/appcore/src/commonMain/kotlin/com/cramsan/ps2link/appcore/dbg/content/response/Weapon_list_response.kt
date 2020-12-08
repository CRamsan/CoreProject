package com.cramsan.ps2link.appcore.dbg.content.response

import com.cramsan.ps2link.appcore.dbg.content.item.Weapon

data class Weapon_list_response(
    var characters_weapon_stat_by_faction_list: List<Weapon>? = null
)
