package com.cramsan.ps2link.network.models.content.character

import com.cramsan.ps2link.network.models.content.world.Name_Multi

data class Server(
    var name: Name_Multi? = null,
    var state: String? = null,
    var world_id: Int = 0,
)
