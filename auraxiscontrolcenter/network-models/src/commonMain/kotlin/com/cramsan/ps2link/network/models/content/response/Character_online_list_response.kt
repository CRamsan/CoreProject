package com.cramsan.ps2link.network.models.content.response

import com.cramsan.ps2link.network.models.content.CharacterProfile
import kotlinx.serialization.Serializable

@Serializable
data class Character_online_list_response(
    var characters_online_status_list: List<CharacterProfile>
)
