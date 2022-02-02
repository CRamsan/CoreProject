package com.cramsan.ps2link.network.models.content

import com.cramsan.ps2link.network.models.content.world.NameMultiLang
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class World(
    val name: NameMultiLang? = null,
    val world_id: String? = null,
    @Contextual
    val state: OnlineStatus? = null,
)
