package com.cesarandres.ps2link

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.cramsan.ps2link.appcore.dbg.Faction
import com.cramsan.ps2link.appcore.dbg.LoginStatus

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
fun LoginStatus?.toStatusString() = when (this) {
    LoginStatus.ONLINE -> stringResource(R.string.text_online)
    LoginStatus.OFFLINE -> stringResource(R.string.text_offline)
    else -> stringResource(R.string.text_unknown)
}
