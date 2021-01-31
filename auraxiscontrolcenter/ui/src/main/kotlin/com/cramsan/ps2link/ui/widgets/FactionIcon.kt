package com.cramsan.ps2link.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.loadImageResource
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.core.models.Faction
import com.cramsan.ps2link.ui.R
import com.cramsan.ps2link.ui.theme.PS2Theme

@Composable
fun FactionIcon(
    modifier: Modifier = Modifier,
    faction: Faction,
) {
    val resourceId = when (faction) {
        Faction.VS -> R.drawable.icon_faction_vs
        Faction.NC -> R.drawable.icon_faction_nc
        Faction.TR -> R.drawable.icon_faction_tr
        Faction.NS -> R.drawable.icon_faction_ns
        Faction.UNKNOWN -> R.drawable.icon_faction_ns
    }

    val image = loadImageResource(id = resourceId)
    image.resource.resource?.let {
        Image(
            bitmap = it,
            modifier = modifier,
        )
    }
}

@Preview
@Composable
fun FactionIconPreview() {
    PS2Theme {
        Column {
            FactionIcon(faction = Faction.VS)
            FactionIcon(faction = Faction.NC)
            FactionIcon(faction = Faction.TR)
            FactionIcon(faction = Faction.NS)
        }
    }
}
