package com.cramsan.ps2link.network.models.content.response

import com.cramsan.ps2link.network.models.content.CharacterEvent
import kotlinx.serialization.Serializable

@Serializable
data class Characters_event_list_response(
    val characters_event_list: List<CharacterEvent>? = null
)
