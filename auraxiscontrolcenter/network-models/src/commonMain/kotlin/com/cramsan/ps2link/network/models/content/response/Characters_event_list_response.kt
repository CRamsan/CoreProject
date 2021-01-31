package com.cramsan.ps2link.network.models.content.response

import com.cramsan.ps2link.network.models.content.CharacterEvent

data class Characters_event_list_response(
    var characters_event_list: List<CharacterEvent>? = null
)
