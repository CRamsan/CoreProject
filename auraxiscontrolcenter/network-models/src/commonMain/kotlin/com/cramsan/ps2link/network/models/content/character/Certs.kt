package com.cramsan.ps2link.network.models.content.character

import kotlinx.serialization.Serializable

@Serializable
data class Certs(
    val available_points: String? = null,
    val percent_to_next: String? = null,
)
