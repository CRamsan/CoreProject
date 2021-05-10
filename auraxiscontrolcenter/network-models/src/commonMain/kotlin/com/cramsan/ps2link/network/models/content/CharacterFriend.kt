package com.cramsan.ps2link.network.models.content

import com.cramsan.ps2link.network.models.content.character.Name
import kotlinx.serialization.Serializable

@Serializable
data class CharacterFriend(
    var name: Name? = null,
    var character_id: String? = null,
    var last_login_time: String? = null,
    var online: String? = null,
)
