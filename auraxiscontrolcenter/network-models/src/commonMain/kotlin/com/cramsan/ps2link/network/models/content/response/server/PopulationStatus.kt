package com.cramsan.ps2link.network.models.content.response.server

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class PopulationStatus {
    @SerialName("high")
    HIGH,
    @SerialName("medium")
    MEDIUM,
    @SerialName("low")
    LOW,
}
