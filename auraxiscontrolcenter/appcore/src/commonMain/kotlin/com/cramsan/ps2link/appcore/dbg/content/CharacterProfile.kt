package com.cramsan.ps2link.appcore.dbg.content

import com.cramsan.ps2link.appcore.dbg.Namespace
import com.cramsan.ps2link.appcore.dbg.content.character.BattleRank
import com.cramsan.ps2link.appcore.dbg.content.character.Certs
import com.cramsan.ps2link.appcore.dbg.content.character.Name
import com.cramsan.ps2link.appcore.dbg.content.character.Server
import com.cramsan.ps2link.appcore.dbg.content.character.Stats
import com.cramsan.ps2link.appcore.dbg.content.character.Times

data class CharacterProfile(
    var character_id: String,
    var name: Name? = null,
    var active_profile_id: String? = null,
    var certs: Certs? = null,
    var battle_rank: BattleRank? = null,
    var times: Times? = null,
    var faction_id: String? = null,
    var world_id: String? = null,
    var outfit: Outfit? = null,
    var outfitName: String? = null,
    var profile: Profile? = null,
    var profile_id: String? = null,
    var stats: Stats? = null,
    var server: Server? = null,
    var online_status: Int = 0,
    var isCached: Boolean = false,
    var character_id_join_character: CharacterProfile? = null,
    var namespace: Namespace? = null,
)
