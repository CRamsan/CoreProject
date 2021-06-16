package com.cramsan.ps2link.network.models.content.response

import com.cramsan.ps2link.network.models.content.CharacterFriendRoot
import kotlinx.serialization.Serializable

@Serializable
data class Character_friend_list_response(
    val characters_friend_list: List<CharacterFriendRoot>
)
