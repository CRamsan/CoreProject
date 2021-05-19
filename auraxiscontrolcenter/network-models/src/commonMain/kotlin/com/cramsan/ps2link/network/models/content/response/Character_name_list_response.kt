package com.cramsan.ps2link.network.models.content.response

import com.cramsan.ps2link.network.models.content.CharacterProfileJoin
import kotlinx.serialization.Serializable

@Serializable
data class Character_name_list_response(
    var character_name_list: List<CharacterProfileJoin>
)
