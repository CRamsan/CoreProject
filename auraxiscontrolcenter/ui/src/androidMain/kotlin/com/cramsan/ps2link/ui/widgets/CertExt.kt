package com.cramsan.ps2link.ui.widgets

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.cramsan.ps2link.ui.R

@Composable
internal actual fun CertPainter(): Painter {
    return painterResource(id = R.drawable.cert)
}