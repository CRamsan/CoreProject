package com.cramsan.ps2link.network.models.content.character

import kotlinx.serialization.Serializable

@Serializable
data class Times(
    val last_login: String? = null,
    val minutes_played: String? = null,
)
