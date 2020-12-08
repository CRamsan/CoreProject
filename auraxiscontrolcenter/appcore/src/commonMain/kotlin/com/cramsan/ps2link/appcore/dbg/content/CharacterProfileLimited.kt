package com.cramsan.ps2link.appcore.dbg.content

import com.cramsan.ps2link.appcore.dbg.content.character.BattleRank
import com.cramsan.ps2link.appcore.dbg.content.character.Name

data class CharacterProfileLimited (
    var character_Id: String? = null,
    var name: Name? = null,
    var faction_id: String? = null,
    var battle_rank: BattleRank? = null,
)