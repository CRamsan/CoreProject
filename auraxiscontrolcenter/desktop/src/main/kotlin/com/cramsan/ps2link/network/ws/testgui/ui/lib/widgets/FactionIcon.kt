package com.cramsan.ps2link.network.ws.testgui.ui.lib.widgets

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.cramsan.ps2link.core.models.Faction

@Composable
fun FactionIcon(
    modifier: Modifier = Modifier,
    faction: Faction,
) {
    val resourceId = when (faction) {
        Faction.VS -> "icon_faction_vs.webp"
        Faction.NC -> "icon_faction_nc.webp"
        Faction.TR -> "icon_faction_tr.webp"
        Faction.NS -> "icon_faction_ns.webp"
        Faction.UNKNOWN -> "icon_faction_ns.webp"
    }

    // TODO: Add content description
    Image(
        modifier = modifier,
        painter = painterResource(resourceId),
        contentDescription = null,
    )
}
