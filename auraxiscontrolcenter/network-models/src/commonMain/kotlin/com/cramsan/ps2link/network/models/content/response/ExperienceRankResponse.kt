package com.cramsan.ps2link.network.models.content.response

import com.cramsan.ps2link.network.models.content.ExperienceRank
import kotlinx.serialization.Serializable

/**
 * Response from the Experience Rank collection.
 * Example: https://census.daybreakgames.com/s:ps2link/get/ps2/experience_rank/?rank=88&c:limit=100
 */
@Serializable
data class ExperienceRankResponse(
    val experience_rank_list: List<ExperienceRank>,
    val returned: Int? = 0,
)
