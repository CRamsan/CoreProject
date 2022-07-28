package com.cramsan.ps2link.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

val ps2Palette = Colors(
    primary = primaryColor,
    primaryVariant = primaryLightColor,
    secondary = secondaryColor,
    secondaryVariant = secondaryLightColor,
    background = primaryDarkColor,
    surface = primaryDarkColor,
    onPrimary = primaryTextColor,
    onSecondary = secondaryTextColor,
    onBackground = primaryTextColor,
    onSurface = primaryTextColor,
    error = errorColor,
    onError = primaryTextColor,
    isLight = false,
)

/**
 * Main theme for this app.
 */
@Composable
fun PetProjectTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    // The PS2 app does not support dark/light theme
    val palette = if (darkTheme) {
        ps2Palette
    } else {
        ps2Palette
    }
    MaterialTheme(
        colors = palette,
        content = content,
    )
}
