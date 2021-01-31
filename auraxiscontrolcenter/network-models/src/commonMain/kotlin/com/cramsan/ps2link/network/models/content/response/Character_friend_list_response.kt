package com.cramsan.ps2link.network.models.content.response

import com.cramsan.ps2link.network.models.content.CharacterFriendRoot

data class Character_friend_list_response(
    var characters_friend_list: List<CharacterFriendRoot>? = null
)
