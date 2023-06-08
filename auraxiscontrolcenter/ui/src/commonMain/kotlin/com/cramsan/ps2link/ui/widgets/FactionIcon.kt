package com.cramsan.ps2link.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cramsan.ps2link.core.models.Faction
import com.cramsan.ps2link.ui.toPainter

@Composable
fun FactionIcon(
    modifier: Modifier = Modifier,
    faction: Faction,
) {
    // TODO: Add content description
    Image(
        modifier = modifier,
        painter = faction.toPainter(),
        contentDescription = null,
    )
}
