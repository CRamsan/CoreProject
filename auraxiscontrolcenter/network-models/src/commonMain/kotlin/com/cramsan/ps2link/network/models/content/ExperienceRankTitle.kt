package com.cramsan.ps2link.network.models.content

import com.cramsan.ps2link.network.models.content.world.NameMultiLang
import kotlinx.serialization.Serializable

/**
 * Example: https://census.daybreakgames.com/get/ps2:v2/experience_rank?&c:limit=300
 */
@Serializable
data class ExperienceRankTitle(
    val image_set_id: String? = null,
    val image_id: String? = null,
    val title: NameMultiLang? = null,
)
