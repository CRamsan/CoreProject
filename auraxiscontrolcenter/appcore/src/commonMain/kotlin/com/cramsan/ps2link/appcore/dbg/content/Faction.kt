package com.cramsan.ps2link.appcore.dbg.content

import com.cramsan.ps2link.appcore.dbg.content.world.Name_Multi

data class Faction(
    var id: String? = null,
    var name: Name_Multi? = null,
    var code: String? = null,
    var icon: String? = null,
) {
    companion object {
        val VS = "1"
        val NC = "2"
        val TR = "3"
    }
}