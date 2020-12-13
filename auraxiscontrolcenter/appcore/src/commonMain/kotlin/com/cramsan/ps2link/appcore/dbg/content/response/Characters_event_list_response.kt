package com.cramsan.ps2link.appcore.dbg.content.response

import com.cramsan.ps2link.appcore.dbg.content.CharacterEvent

data class Characters_event_list_response(
    var characters_event_list: List<CharacterEvent>? = null
)
