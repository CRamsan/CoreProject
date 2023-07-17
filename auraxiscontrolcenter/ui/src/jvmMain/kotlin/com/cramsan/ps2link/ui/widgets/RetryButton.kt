package com.cramsan.ps2link.ui.widgets

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cramsan.ps2link.ui.SlimButton

@Composable
actual fun RetryButton(modifier: Modifier, onRefreshRequested: () -> Unit) {
    SlimButton(
        onClick = { onRefreshRequested() },
        modifier = Modifier,
    ) { Text("Retry") }
}
