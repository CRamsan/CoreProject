package com.cramsan.ps2link.network.models.content.world

import kotlinx.serialization.Serializable

/**
 * Used for names that change depending on language. For example weapon names, directive names, etc.
 * Example: https://census.daybreakgames.com/get/ps2:v2/experience_rank?&c:limit=300
 */
@Serializable
data class NameMultiLang(
    val de: String? = null,
    val en: String? = null,
    val es: String? = null,
    val fr: String? = null,
    val it: String? = null,
    val tr: String? = null,
)
