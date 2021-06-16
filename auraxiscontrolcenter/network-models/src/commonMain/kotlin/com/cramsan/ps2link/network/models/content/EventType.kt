package com.cramsan.ps2link.network.models.content

import com.cramsan.ps2link.network.models.content.world.Name_Multi
import kotlinx.serialization.Serializable

@Serializable
data class EventType(
    val name: Name_Multi? = null,
    val description: Description? = null,
)
