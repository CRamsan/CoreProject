package com.cesarandres.ps2link

import com.cramsan.ps2link.appcore.dbg.Faction

/**
 * @Author cramsan
 * @created 1/17/2021
 */

fun Faction.toUIFaction(): com.cramsan.ps2link.ui.Faction {
    return when (this) {
        Faction.VS -> com.cramsan.ps2link.ui.Faction.VS
        Faction.NC -> com.cramsan.ps2link.ui.Faction.NC
        Faction.TR -> com.cramsan.ps2link.ui.Faction.TR
        Faction.NS -> com.cramsan.ps2link.ui.Faction.NS
        Faction.UNKNOWN -> com.cramsan.ps2link.ui.Faction.UNKNOWN
    }
}
