package com.cramsan.ps2link.network.models.content.response.server

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LiveServersPS4(

    @SerialName("Ceres (EU)")
    val ceres: LiveServer? = null,

    @SerialName("Genudine")
    val genudine: LiveServer? = null,
)
