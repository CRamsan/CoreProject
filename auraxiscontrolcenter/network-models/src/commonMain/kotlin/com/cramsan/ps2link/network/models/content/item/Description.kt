package com.cramsan.ps2link.network.models.content.item

import kotlinx.serialization.Serializable

@Serializable
data class Description(
    val de: String? = null,
    val en: String? = null,
    val es: String? = null,
    val fr: String? = null,
    val it: String? = null,
    val tr: String? = null,
)
