package com.cramsan.ps2link.ui.theme

import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.Shapes
import androidx.compose.ui.unit.dp
import com.cramsan.ps2link.ui.theme.Shapes.large
import com.cramsan.ps2link.ui.theme.Shapes.medium
import com.cramsan.ps2link.ui.theme.Shapes.small

object Shapes {
    val small = CutCornerShape(topRight = 10.dp, bottomLeft = 10.dp)
    val medium = CutCornerShape(topRight = 12.dp, bottomLeft = 12.dp)
    val large = CutCornerShape(topRight = 16.dp, bottomLeft = 16.dp)
    val largeBottom = CutCornerShape(bottomLeft = 16.dp, bottomRight = 16.dp)
}

val shapes = Shapes(
    small = small,
    medium = medium,
    large = large
)
