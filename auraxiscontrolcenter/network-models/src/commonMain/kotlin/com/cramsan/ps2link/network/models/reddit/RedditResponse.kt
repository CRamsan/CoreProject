package com.cramsan.ps2link.network.models.reddit

import kotlinx.serialization.Serializable

/**
 * @Author cramsan
 * @created 2/5/2021
 */
@Serializable
data class RedditResponse(
    val data: Data,
)
