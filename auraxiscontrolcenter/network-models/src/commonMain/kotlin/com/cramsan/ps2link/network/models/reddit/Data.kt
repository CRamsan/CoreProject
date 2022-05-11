package com.cramsan.ps2link.network.models.reddit

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val children: List<PostData>,
)
