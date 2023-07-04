package com.cramsan.ps2link.network.models.content.response

import com.cramsan.ps2link.network.models.content.CharacterProfile
import kotlinx.serialization.Serializable

@Serializable
data class Character_list_response(
    val character_list: List<CharacterProfile>? = null,
    val returned: Long? = null,
)
