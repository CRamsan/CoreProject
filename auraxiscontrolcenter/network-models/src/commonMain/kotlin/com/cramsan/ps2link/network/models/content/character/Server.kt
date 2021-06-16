package com.cramsan.ps2link.network.models.content.character

import com.cramsan.ps2link.network.models.content.world.Name_Multi
import kotlinx.serialization.Serializable

@Serializable
data class Server(
    val name: Name_Multi? = null,
    val state: String? = null,
    val world_id: Int? = 0,
)
