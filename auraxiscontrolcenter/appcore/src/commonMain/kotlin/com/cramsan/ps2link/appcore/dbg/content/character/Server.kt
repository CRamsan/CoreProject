package com.cramsan.ps2link.appcore.dbg.content.character

import com.cramsan.ps2link.appcore.dbg.content.world.Name_Multi

data class Server(
    var name: Name_Multi? = null,
    var state: String? = null,
    var world_id: Int = 0,
)
