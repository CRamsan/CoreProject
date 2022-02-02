package com.cramsan.ps2link.network.models.content

import kotlinx.serialization.Serializable

/**
 * Example: https://census.daybreakgames.com/get/ps2:v2/experience_rank?&c:limit=300
 */
@Serializable
data class ExperienceRank(
    val rank: String? = null,
    val xp_max: String? = null,
    val vs_image_path: String? = null,
    val nc_image_path: String? = null,
    val tr_image_path: String? = null,
    val vs: ExperienceRankTitle? = null,
    val tr: ExperienceRankTitle? = null,
    val nc: ExperienceRankTitle? = null,
)
