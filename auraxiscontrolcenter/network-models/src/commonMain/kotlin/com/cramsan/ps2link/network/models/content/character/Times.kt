package com.cramsan.ps2link.network.models.content.character

import kotlinx.serialization.Serializable

/**
 * Source: https://census.daybreakgames.com/s:PS2LinkPreProd/get/ps2:v2/character//5428010618041058369?c%3Aresolve=outfit%2Cworld%2Conline_status&c%3Ajoin=type%3Aworld%5Einject_at%3Aserver&c%3Alang=en
 */
@Serializable
@Suppress("UndocumentedPublicProperty", "ConstructorParameterNaming")
data class Times(
    val last_login: String? = null,
    val minutes_played: String? = null,
    val creation: String? = null,
    val login_count: String? = null,
)
