package com.cramsan.ps2link.network.models.content.character

import kotlinx.serialization.Serializable

@Suppress("ConstructorParameterNaming")
@Serializable
data class Certs(
    val available_points: String? = null,
    val percent_to_next: String? = null,
    val earned_points: String? = null,
    val gifted_points: String? = null,
    val spent_points: String? = null,
)
