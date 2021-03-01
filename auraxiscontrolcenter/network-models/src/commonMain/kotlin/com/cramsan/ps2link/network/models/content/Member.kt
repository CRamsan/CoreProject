package com.cramsan.ps2link.network.models.content

import com.cramsan.ps2link.network.models.content.character.Name
import kotlinx.serialization.Serializable

@Serializable
data class Member(
    var character_id: String? = null,
    var online_status: String? = null,
    var rank: String? = null,
    var outfit_id: String? = null,
    var character: CharacterProfile? = null,
    var name: Name? = null,
)
