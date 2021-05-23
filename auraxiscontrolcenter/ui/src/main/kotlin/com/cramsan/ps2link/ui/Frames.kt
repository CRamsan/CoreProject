package com.cramsan.ps2link.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.cramsan.ps2link.core.models.Faction
import com.cramsan.ps2link.ui.theme.Opacity
import com.cramsan.ps2link.ui.theme.PS2Theme
import com.cramsan.ps2link.ui.theme.Padding
import com.cramsan.ps2link.ui.theme.Shapes
import com.cramsan.ps2link.ui.theme.Size
import com.cramsan.ps2link.ui.widgets.FactionIcon

@Composable
private fun Frame(
    modifier: Modifier,
    shape: Shape,
    border: BorderStroke,
    marginPadding: Dp,
    alignment: Alignment = Alignment.TopCenter,
    content: @Composable() () -> Unit,
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
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.TopCenter,
    content: @Composable() () -> Unit,
) {
    Frame(
        modifier = modifier.fillMaxSize(),
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
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.TopCenter,
    content: @Composable() () -> Unit,
) {
    Frame(
        modifier = modifier.fillMaxSize(),
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
    content: @Composable() () -> Unit,
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

@Preview(name = "Bottom Frame")
@Composable
fun FrameBottomPreview() {
    PS2Theme {
        FrameBottom {
            MainMenuButton(label = "Android")
        }
    }
}

@Preview(name = "Frame Center")
@Composable
fun FrameCenterPreview() {
    PS2Theme {
        FrameCenter {
            MainMenuButton(label = "Android")
        }
    }
}

@Preview(name = "Frame Slim")
@Composable
fun FrameSlimPreview() {
    PS2Theme {
        FrameSlim {
            FactionIcon(faction = Faction.VS)
        }
    }
}
