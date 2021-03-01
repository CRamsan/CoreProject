package com.cramsan.ps2link.network.models.content.response.server

import kotlinx.serialization.Serializable

@Serializable
data class LiveServer(
    var age: String? = null,
    var ageSeconds: Int = 0,
    var status: PopulationStatus? = null,
)
