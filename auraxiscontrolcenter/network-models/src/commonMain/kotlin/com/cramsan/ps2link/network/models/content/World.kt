package com.cramsan.ps2link.network.models.content

import com.cramsan.ps2link.network.models.content.world.Name_Multi

data class World(
    var name: Name_Multi? = null,
    var world_id: String? = null,
    var character_id: String? = null,
    var state: String? = null,
    var population: String? = null,
    var isRegistered: Boolean = false,
    var lastAlert: WorldEvent? = null,
)
