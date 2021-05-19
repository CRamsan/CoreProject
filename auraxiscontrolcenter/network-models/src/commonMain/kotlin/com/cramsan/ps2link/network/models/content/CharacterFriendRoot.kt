package com.cramsan.ps2link.network.models.content

import kotlinx.serialization.Serializable

@Serializable
data class CharacterFriendRoot(
    var character_id: String? = null,
    var name: String? = null,
    var friend_list: List<CharacterFriend>
)
