package com.cramsan.ps2link.network.ws.testgui.ui.lib.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.unit.sp

val PS2Font = FontFamily(
    Font(resource = "planetside2.ttf"),
)

// Set of Material typography styles to start with
val typography = Typography(
    defaultFontFamily = PS2Font,
    body1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
    ),
)
