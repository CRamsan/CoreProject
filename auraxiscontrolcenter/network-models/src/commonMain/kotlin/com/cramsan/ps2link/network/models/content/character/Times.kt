package com.cramsan.ps2link.network.models.content.character

import kotlinx.serialization.Serializable

@Serializable
data class Times(
    var last_login: String? = null,
    var minutes_played: String? = null,
)
