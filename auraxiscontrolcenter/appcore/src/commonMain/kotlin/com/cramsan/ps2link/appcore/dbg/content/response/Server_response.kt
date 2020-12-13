package com.cramsan.ps2link.appcore.dbg.content.response

import com.cramsan.ps2link.appcore.dbg.content.World

data class Server_response(
    var world_list: List<World>? = null
)
