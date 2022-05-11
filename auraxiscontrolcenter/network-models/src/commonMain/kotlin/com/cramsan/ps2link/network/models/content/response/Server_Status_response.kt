package com.cramsan.ps2link.network.models.content.response

import com.cramsan.ps2link.network.models.content.response.server.PS2
import kotlinx.serialization.Serializable

@Serializable
data class Server_Status_response(
    val ps2: PS2,
)
