package com.cramsan.petproject.about

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.ui.theme.PetProjectTheme

@Preview(
    widthDp = 900,
    heightDp = 400,
    showBackground = true,
)
@Composable
private fun AboutPreview() {
    PetProjectTheme {
        AboutScreen()
    }
}
