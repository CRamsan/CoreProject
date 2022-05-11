package com.cramsan.ps2link.network.models.content

import kotlinx.serialization.Serializable

@Serializable
data class CharacterFriendRoot(
    val character_id: String? = null,
    val name: String? = null,
    val friend_list: List<CharacterFriend>,
)
