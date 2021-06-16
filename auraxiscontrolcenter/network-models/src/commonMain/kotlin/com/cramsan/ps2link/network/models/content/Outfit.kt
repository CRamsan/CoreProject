package com.cramsan.ps2link.network.models.content

import kotlinx.serialization.Serializable

@Serializable
data class Outfit(
    val outfit_id: String,
    val name: String? = null,
    val alias: String? = null,
    val leader_character_id: String? = null,
    val member_count: Int = 0,
    val time_created: String? = null,
    val members: List<CharacterProfile>? = null,
    val leader: CharacterProfileNullId? = null,
)
