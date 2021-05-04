package com.cramsan.ps2link.network.models.content

import kotlinx.serialization.Serializable

@Serializable
data class Outfit(
    var outfit_id: String,
    var name: String? = null,
    var alias: String? = null,
    var leader_character_id: String? = null,
    var member_count: Int = 0,
    var time_created: String? = null,
    var members: List<CharacterProfile>? = null,
    var leader: CharacterProfileNullId? = null,
)
