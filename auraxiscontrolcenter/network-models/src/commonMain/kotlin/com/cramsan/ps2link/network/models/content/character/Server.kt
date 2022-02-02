package com.cramsan.ps2link.network.models.content.character

import com.cramsan.ps2link.network.models.content.world.NameMultiLang
import kotlinx.serialization.Serializable

@Serializable
data class Server(
    val name: NameMultiLang? = null,
    val state: String? = null,
    val world_id: Int? = 0,
)
