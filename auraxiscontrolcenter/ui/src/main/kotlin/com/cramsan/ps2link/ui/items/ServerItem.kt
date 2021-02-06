package com.cramsan.ps2link.ui.items

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.core.models.Population
import com.cramsan.ps2link.core.models.ServerStatus
import com.cramsan.ps2link.ui.SlimButton
import com.cramsan.ps2link.ui.theme.PS2Theme

@Composable
fun ServerItem(
    modifier: Modifier = Modifier,
    serverName: String,
    population: Population = Population.UNKNOWN,
    status: ServerStatus = ServerStatus.UNKNOWN,
) {
    SlimButton(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = serverName,
            )
        }
    }
}

@Preview
@Composable
fun ServerItemPreview() {
    PS2Theme {
        FriendItem(
            label = "Connery",
        )
    }
}
