package com.cramsan.ps2link.network.ws.testgui.ui.lib

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.cramsan.ps2link.network.ws.testgui.ui.lib.theme.Opacity
import com.cramsan.ps2link.network.ws.testgui.ui.lib.theme.Padding
import com.cramsan.ps2link.network.ws.testgui.ui.lib.theme.Shapes
import com.cramsan.ps2link.network.ws.testgui.ui.lib.theme.Size

@Composable
private fun Frame(
    modifier: Modifier,
    shape: Shape,
    border: BorderStroke,
    marginPadding: Dp,
    alignment: Alignment = Alignment.TopCenter,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = shape,
        border = border,
        color = MaterialTheme.colors.primary.setAlpha(Opacity.transparent),
        contentColor = MaterialTheme.colors.onPrimary,
    ) {
        Box(
            modifier = Modifier.padding(marginPadding),
            contentAlignment = alignment,
        ) {
            content()
        }
    }
}

@Composable
fun FrameBottom(
    modifier: Modifier = Modifier.fillMaxSize(),
    alignment: Alignment = Alignment.TopCenter,
    content: @Composable() () -> Unit,
) {
    Frame(
        modifier = modifier,
        shape = Shapes.largeBottom,
        border = BorderStroke(Size.xsmall, MaterialTheme.colors.primary),
        marginPadding = Padding.medium,
        alignment = alignment,
    ) {
        content()
    }
}

@Composable
fun FrameCenter(
    modifier: Modifier = Modifier.fillMaxSize(),
    alignment: Alignment = Alignment.TopCenter,
    content: @Composable() () -> Unit,
) {
    Frame(
        modifier = modifier,
        shape = RectangleShape,
        border = BorderStroke(Size.xsmall, MaterialTheme.colors.primary),
        marginPadding = Padding.medium,
        alignment = alignment,
    ) {
        content()
    }
}

@Composable
fun FrameSlim(
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.TopCenter,
    content: @Composable () -> Unit,
) {
    Frame(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(Size.xmicro, MaterialTheme.colors.primary),
        marginPadding = Padding.xsmall,
        alignment = alignment,
    ) {
        content()
    }
}
