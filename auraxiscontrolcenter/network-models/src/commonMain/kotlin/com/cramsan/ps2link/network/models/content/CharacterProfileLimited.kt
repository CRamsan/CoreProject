package com.cramsan.ps2link.network.models.content

import com.cramsan.ps2link.network.models.content.character.BattleRank
import com.cramsan.ps2link.network.models.content.character.Name
import kotlinx.serialization.Serializable

@Serializable
data class CharacterProfileLimited(
    var character_Id: String? = null,
    var name: Name? = null,
    var faction_id: String? = null,
    var battle_rank: BattleRank? = null,
)
