package com.cramsan.ps2link.network.ws.testgui.ui.lib.theme

import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.Shapes
import androidx.compose.ui.unit.dp

object Shapes {
    val small = CutCornerShape(topEnd = 10.dp, bottomStart = 10.dp)
    val medium = CutCornerShape(topEnd = 12.dp, bottomStart = 12.dp)
    val large = CutCornerShape(topEnd = 16.dp, bottomStart = 16.dp)
    val largeBottom = CutCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
}

val shapes = Shapes(
    small = com.cramsan.ps2link.network.ws.testgui.ui.lib.theme.Shapes.small,
    medium = com.cramsan.ps2link.network.ws.testgui.ui.lib.theme.Shapes.medium,
    large = com.cramsan.ps2link.network.ws.testgui.ui.lib.theme.Shapes.large,
)
