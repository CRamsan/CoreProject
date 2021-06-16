package com.cramsan.ps2link.network.models.content

import com.cramsan.ps2link.network.models.content.world.Name_Multi

data class Faction(
    val id: String? = null,
    val name: Name_Multi? = null,
    val code: String? = null,
    val icon: String? = null,
) {
    companion object {
        val VS = "1"
        val NC = "2"
        val TR = "3"
    }
}
