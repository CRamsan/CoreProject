package com.cramsan.ps2link.network.ws.testgui.ui.lib.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

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

@Composable
fun PS2Theme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    // The PS2 app does not support dark/light theme
    val palette = if (darkTheme) {
        ps2Palette
    } else {
        ps2Palette
    }
    MaterialTheme(
        colors = palette,
        typography = typography,
        shapes = shapes,
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Black,
        ) {
            content()
        }
    }
}
