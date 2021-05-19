package com.cramsan.ps2link.network.models.content.response

import com.cramsan.ps2link.network.models.content.World
import kotlinx.serialization.Serializable

@Serializable
data class Server_response(
    var world_list: List<World>
)
