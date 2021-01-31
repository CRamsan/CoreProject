package com.cramsan.ps2link.network.models.content.response

import com.cramsan.ps2link.network.models.content.World

data class Server_response(
    var world_list: List<World>? = null
)
