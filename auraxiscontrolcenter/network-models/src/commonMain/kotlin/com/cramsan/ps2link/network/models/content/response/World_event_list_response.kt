package com.cramsan.ps2link.network.models.content.response

import com.cramsan.ps2link.network.models.content.WorldEvent
import kotlinx.serialization.Serializable

@Serializable
data class World_event_list_response(
    val world_event_list: List<WorldEvent>
)
