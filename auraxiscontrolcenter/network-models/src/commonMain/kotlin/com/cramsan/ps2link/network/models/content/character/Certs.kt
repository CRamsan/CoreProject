package com.cramsan.ps2link.network.models.content.character

import kotlinx.serialization.Serializable

@Serializable
data class Certs(
    var available_points: String? = null,
    var percent_to_next: String? = null,
)
