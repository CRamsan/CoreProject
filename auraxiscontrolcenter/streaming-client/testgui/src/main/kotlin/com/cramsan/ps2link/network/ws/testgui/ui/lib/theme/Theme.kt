package com.cramsan.ps2link.network.ws.testgui.ui.lib.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Colors
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.cramsan.ps2link.network.ws.testgui.ui.lib.BoldButton

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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PS2Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    onClose: () -> Unit,
    onMinimize: () -> Unit,
    content: @Composable () -> Unit,
) {
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
            shape = Shapes.large,
            border = BorderStroke(Size.micro, primaryColor),
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                content()
                Row(
                    modifier = Modifier.align(Alignment.TopEnd).background(color = Color.Black, shape = Shapes.small),
                ) {
                    BoldButton(onClick = onMinimize, modifier = Modifier.height(Size.xlarge)) { Text("-") }
                    BoldButton(onClick = onClose, modifier = Modifier.height(Size.xlarge)) { Text("x") }
                }
            }
        }
    }
}
