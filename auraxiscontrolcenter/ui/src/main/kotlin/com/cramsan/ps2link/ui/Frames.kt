package com.cramsan.ps2link.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.ui.theme.Opacity
import com.cramsan.ps2link.ui.theme.PS2Theme
import com.cramsan.ps2link.ui.theme.Shapes
import com.cramsan.ps2link.ui.theme.Size

@Composable
private fun Frame(
    modifier: Modifier,
    shape: Shape,
    content: @Composable() () -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = shape,
        border = BorderStroke(Size.xsmall, MaterialTheme.colors.primary),
        color = MaterialTheme.colors.primary.setAlpha(Opacity.transparent),
        contentColor = MaterialTheme.colors.onPrimary,
    ) {
        content()
    }
}

@Composable
fun FrameBottom(
    modifier: Modifier = Modifier,
    content: @Composable() () -> Unit,
) {
    Frame(
        modifier = modifier,
        shape = Shapes.largeBottom
    ) {
        content()
    }
}

@Composable
fun FrameCenter(
    modifier: Modifier = Modifier,
    content: @Composable() () -> Unit,
) {
    Frame(
        modifier = modifier,
        shape = RectangleShape
    ) {
        content()
    }
}

@Composable
fun FrameSlim(
    modifier: Modifier = Modifier,
    content: @Composable() () -> Unit,
) {
    Frame(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium
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
            MainMenuButton(label = "Android")
        }
    }
}
