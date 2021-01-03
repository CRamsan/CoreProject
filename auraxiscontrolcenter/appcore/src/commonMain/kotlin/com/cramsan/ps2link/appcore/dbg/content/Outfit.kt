package com.cramsan.ps2link.appcore.dbg.content

import com.cramsan.ps2link.appcore.dbg.Namespace

data class Outfit(
    var outfit_id: String,
    var name: String? = null,
    var alias: String? = null,
    var leader_character_id: String? = null,
    var member_count: Int = 0,
    var time_created: String? = null,
    var world_id: String? = null,
    var faction_id: String? = null,
    var isCached: Boolean = false,
    var namespace: Namespace? = null,
    var members: List<Member>? = null,
    var leader: CharacterProfile? = null,
)
