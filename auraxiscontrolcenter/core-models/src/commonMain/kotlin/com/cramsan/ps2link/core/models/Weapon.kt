package com.cramsan.ps2link.core.models

import kotlinx.serialization.Serializable

@Serializable
data class Weapon(
    val id: String,
    val name: String? = null,
    val imageUrl: String? = null,
)
