package com.cramsan.ps2link.network.models.content.character

import kotlinx.serialization.Serializable

@Serializable
data class Name(
    val first: String? = null,
    val first_lower: String? = null,
)
