package com.cramsan.ps2link.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable

@Composable
@Preview
fun PreviewErrorOverlay() {
    ErrorOverlay(
        isError = true,
        error = "There was an error, please try again",
        onRefreshRequested = { }
    )
}
