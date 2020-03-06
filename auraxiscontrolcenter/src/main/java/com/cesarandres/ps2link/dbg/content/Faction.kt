package com.cesarandres.ps2link.dbg.content

import com.cesarandres.ps2link.dbg.content.world.Name_Multi

class Faction {

    var id: String? = null
    var name: Name_Multi? = null
    var code: String? = null
    var icon: String? = null

    companion object {

        val VS = "1"
        val NC = "2"
        val TR = "3"
    }
}
