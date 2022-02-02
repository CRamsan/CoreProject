package com.cramsan.ps2link.core.models

import kotlinx.serialization.Serializable

/**
 * @author cramsan
 *
 * Represents a rank based on the player's battle rank.
 */
@Serializable
@Suppress("UndocumentedPublicProperty")
data class ExperienceRank(
    val rank: Long? = null,
    val xpMax: Long? = null,
    val imageId: String? = null,
    val imagePath: String? = null,
)
