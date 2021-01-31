package com.cramsan.ps2link.ui.items

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.core.models.Faction
import com.cramsan.ps2link.ui.SlimButton
import com.cramsan.ps2link.ui.theme.PS2Theme
import com.cramsan.ps2link.ui.theme.Size
import com.cramsan.ps2link.ui.widgets.BR
import com.cramsan.ps2link.ui.widgets.FactionIcon

@Composable
fun ProfileItem(
    modifier: Modifier = Modifier,
    label: String,
    faction: Faction? = null,
    level: Int? = null,
    onClick: () -> Unit = {},
) {
    SlimButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            faction?.let {
                FactionIcon(modifier = Modifier.size(Size.large), faction = it)
            }

            Text(
                text = label,
                textAlign = TextAlign.Center,
            )

            level?.let {
                BR(level = it)
            }
        }
    }
}

@Preview
@Composable
fun ProfileItemPreview() {
    PS2Theme {
        ProfileItem(
            label = "Cramsan",
            faction = Faction.VS,
            level = 80,
        )
    }
}
