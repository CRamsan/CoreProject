package com.cramsan.ps2link.network.models.content

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class OnlineStatus {
    @SerialName("online")
    ONLINE,
    @SerialName("offline")
    OFFLINE,
    @SerialName("locked")
    LOCKED,
}
