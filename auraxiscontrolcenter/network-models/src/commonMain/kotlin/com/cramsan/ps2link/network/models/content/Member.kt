package com.cramsan.ps2link.network.models.content

import com.cramsan.ps2link.network.models.content.character.Name
import kotlinx.serialization.Serializable

@Serializable
data class Member(
    val character_id: String? = null,
    val online_status: String? = null,
    val rank: String? = null,
    val rank_ordinal: String? = null,
    val outfit_id: String? = null,
    val character: CharacterProfileNullId? = null,
    val name: Name? = null,
)
