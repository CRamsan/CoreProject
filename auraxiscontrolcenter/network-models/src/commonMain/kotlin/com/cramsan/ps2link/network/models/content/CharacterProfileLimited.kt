package com.cramsan.ps2link.network.models.content

import com.cramsan.ps2link.network.models.content.character.BattleRank
import com.cramsan.ps2link.network.models.content.character.Name
import kotlinx.serialization.Serializable

@Serializable
data class CharacterProfileLimited(
    val character_Id: String? = null,
    val name: Name? = null,
    val faction_id: String? = null,
    val battle_rank: BattleRank? = null,
)
