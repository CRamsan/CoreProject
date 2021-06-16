package com.cramsan.ps2link.network.models.content

import com.cramsan.ps2link.network.models.content.character.BattleRank
import com.cramsan.ps2link.network.models.content.character.Certs
import com.cramsan.ps2link.network.models.content.character.Name
import com.cramsan.ps2link.network.models.content.character.Server
import com.cramsan.ps2link.network.models.content.character.Stats
import com.cramsan.ps2link.network.models.content.character.Times
import kotlinx.serialization.Serializable

@Serializable
data class CharacterProfile(
    val character_id: String,
    val name: Name? = null,
    val active_profile_id: String? = null,
    val certs: Certs? = null,
    val battle_rank: BattleRank? = null,
    val times: Times? = null,
    val faction_id: String? = null,
    val world_id: String? = null,
    val outfit: Outfit? = null,
    val outfitName: String? = null,
    val rank: String? = null,
    val rank_ordinal: String? = null,
    val profile: Profile? = null,
    val profile_id: String? = null,
    val stats: Stats? = null,
    val server: Server? = null,
    val online_status: String? = null,
)
