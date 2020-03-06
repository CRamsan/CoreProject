package com.cesarandres.ps2link.dbg.content.response

import com.cesarandres.ps2link.dbg.content.item.Weapon

import java.util.ArrayList


class Weapon_list_response {
    private var characters_weapon_stat_by_faction_list: ArrayList<Weapon>? = null

    fun getcharacters_weapon_stat_by_faction_list(): ArrayList<Weapon>? {
        return characters_weapon_stat_by_faction_list
    }

    fun setcharacters_weapon_stat_by_faction_list(
        characters_weapon_stat_by_faction_list: ArrayList<Weapon>
    ) {
        this.characters_weapon_stat_by_faction_list = characters_weapon_stat_by_faction_list
    }
}