package com.cramsan.ps2link.network.models.content

import com.cramsan.ps2link.network.models.content.character.BattleRank
import com.cramsan.ps2link.network.models.content.character.Certs
import com.cramsan.ps2link.network.models.content.character.Name
import com.cramsan.ps2link.network.models.content.character.Server
import com.cramsan.ps2link.network.models.content.character.Stats
import com.cramsan.ps2link.network.models.content.character.Times
import kotlinx.serialization.Serializable

@Serializable
data class CharacterProfileNullId(
    var character_id: String? = null,
    var name: Name? = null,
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
    var character_id_join_character: CharacterProfileNullId? = null,
)
