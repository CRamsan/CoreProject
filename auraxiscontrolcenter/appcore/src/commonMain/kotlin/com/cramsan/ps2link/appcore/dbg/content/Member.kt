package com.cramsan.ps2link.appcore.dbg.content

import com.cramsan.ps2link.appcore.dbg.content.character.Name

data class Member(
    var character_id: String? = null,
    var online_status: String? = null,
    var rank: String? = null,
    var outfit_id: String? = null,
    var character: CharacterProfile? = null,
    var name: Name? = null,
)