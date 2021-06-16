package com.cramsan.ps2link.network.models.content.response.server

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LiveServers(
    @SerialName("Cobalt (EU)")
    val cobalt: LiveServer? = null,

    @SerialName("Connery (US West)")
    val connery: LiveServer? = null,

    @SerialName("Emerald (US East)")
    val emerald: LiveServer? = null,

    @SerialName("Miller (EU)")
    val miller: LiveServer? = null,
)
