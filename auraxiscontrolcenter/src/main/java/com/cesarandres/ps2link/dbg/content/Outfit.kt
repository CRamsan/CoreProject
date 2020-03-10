package com.cesarandres.ps2link.dbg.content

import com.cesarandres.ps2link.dbg.DBGCensus.Namespace

import java.util.ArrayList

class Outfit {
    var outfit_id: String? = null
    var name: String? = null
    var alias: String? = null
    var leader_character_id: String? = null
    var member_count: Int = 0
    var time_created: String? = null
    var world_id: String? = null
    var faction_id: String? = null
    var isCached: Boolean = false
    var namespace: Namespace? = null
    var members: ArrayList<Member>? = null
    var leader: CharacterProfile? = null

}
