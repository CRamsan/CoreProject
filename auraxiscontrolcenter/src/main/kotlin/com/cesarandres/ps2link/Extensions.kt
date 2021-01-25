package com.cesarandres.ps2link

import androidx.compose.runtime.Composable
import com.cramsan.ps2link.appcore.dbg.Faction
import com.cramsan.ps2link.appcore.dbg.LoginStatus
import com.cramsan.ps2link.ui.OnlineStatus

/**
 * @Author cramsan
 * @created 1/17/2021
 */

fun Faction?.toUIFaction(): com.cramsan.ps2link.ui.Faction {
    return when (this) {
        Faction.VS -> com.cramsan.ps2link.ui.Faction.VS
        Faction.NC -> com.cramsan.ps2link.ui.Faction.NC
        Faction.TR -> com.cramsan.ps2link.ui.Faction.TR
        Faction.NS -> com.cramsan.ps2link.ui.Faction.NS
        else -> com.cramsan.ps2link.ui.Faction.UNKNOWN
    }
}

@Composable
fun LoginStatus?.toOnlineStatus() = when (this) {
    LoginStatus.ONLINE -> OnlineStatus.ONLINE
    LoginStatus.OFFLINE -> OnlineStatus.OFFLINE
    else -> OnlineStatus.UNKNOWN
}
