package com.cramsan.ps2link.network.models.content.response.server

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class LiveServer(
    val age: String? = null,
    val ageSeconds: Int? = 0,
    @Contextual
    val status: PopulationStatus? = null,
)
