package com.cramsan.ps2link.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource

@Composable
internal actual fun StarPainter(): Painter {
    return painterResource(id = R.drawable.icon_star_selected)
}