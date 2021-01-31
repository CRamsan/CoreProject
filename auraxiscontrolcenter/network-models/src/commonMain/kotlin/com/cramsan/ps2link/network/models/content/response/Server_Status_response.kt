package com.cramsan.ps2link.network.models.content.response

import com.cramsan.ps2link.network.models.content.response.server.PS2

data class Server_Status_response(
    var ps2: PS2? = null
)
