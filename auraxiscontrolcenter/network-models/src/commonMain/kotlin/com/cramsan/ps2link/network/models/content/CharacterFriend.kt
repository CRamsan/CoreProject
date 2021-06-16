package com.cramsan.ps2link.network.models.content

import com.cramsan.ps2link.network.models.content.character.Name
import kotlinx.serialization.Serializable

@Serializable
data class CharacterFriend(
    val name: Name? = null,
    val character_id: String? = null,
    val last_login_time: String? = null,
    val online: String? = null,
)
