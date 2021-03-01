package com.cramsan.ps2link.network.models.content.character

import kotlinx.serialization.Serializable

@Serializable
data class Name(
    var first: String? = null,
    var first_lower: String? = null,
)
