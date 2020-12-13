package com.cramsan.ps2link.appcore.dbg.content.response

import com.cramsan.ps2link.appcore.dbg.content.CharacterProfile

data class Character_list_response(
    var character_name_list: List<CharacterProfile>? = null,
    var character_list: List<CharacterProfile>? = null,
    var characters_online_status_list: List<CharacterProfile>? = null,
)
