package com.cesarandres.ps2link.dbg.content

import com.cesarandres.ps2link.dbg.content.world.Name_Multi

class World {
    var name: Name_Multi? = null
    var world_id: String? = null
    var character_id: String? = null
    var state: String? = null
    var population: String? = null
    var isRegistered: Boolean = false
    var lastAlert: WorldEvent? = null

}
