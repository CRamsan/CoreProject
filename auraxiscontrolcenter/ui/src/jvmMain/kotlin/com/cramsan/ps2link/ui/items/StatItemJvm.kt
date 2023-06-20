package com.cramsan.ps2link.ui.items

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable

@Preview
@Composable
fun Preview() {
    StatItemVertical(
        label = "Kills",
        allTime = 1000.0,
        today = 145.0,
        thisWeek = 34.0,
        thisMonth = 234.0,
    )
}
