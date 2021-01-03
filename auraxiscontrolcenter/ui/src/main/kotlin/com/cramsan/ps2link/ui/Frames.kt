package com.cramsan.ps2link.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cramsan.ps2link.ui.theme.PS2Theme
import com.cramsan.ps2link.ui.theme.Shapes
import com.cramsan.ps2link.ui.theme.teal200
import com.cramsan.ps2link.ui.theme.teal900

@Composable
fun FrameBottom(content: @Composable() () -> Unit) {
    val shape = Shapes.largeBottom
    Box(
        modifier = Modifier.background(teal200, shape)
            .clip(shape)
            .border(BorderStroke(3.dp, teal900), shape)
            .padding(
                horizontal = 10.dp,
                vertical = 5.dp
            )
    ) {
        content()
    }
}

@Composable
fun FrameCenter(content: @Composable() () -> Unit) {
    Box(
        modifier = Modifier.background(teal200)
            .border(BorderStroke(3.dp, teal900))
            .padding(
                horizontal = 10.dp,
                vertical = 5.dp
            )
    ) {
        content()
    }
}

@Composable
fun FrameSlim(content: @Composable() () -> Unit) {
    val shape = MaterialTheme.shapes.medium
    Box(
        modifier = Modifier.background(teal200, shape)
            .clip(shape)
            .border(BorderStroke(3.dp, teal900), shape)
            .padding(
                horizontal = 10.dp,
                vertical = 5.dp
            )
    ) {
        content()
    }
}

@Preview(name = "Bottom Frame")
@Composable
fun FrameBottomPreview() {
    PS2Theme {
        FrameBottom {
            BoldButton(label = "Android")
        }
    }
}

@Preview(name = "Frame Center")
@Composable
fun FrameCenterPreview() {
    PS2Theme {
        FrameCenter {
            BoldButton(label = "Android")
        }
    }
}

@Preview(name = "Frame Slim")
@Composable
fun FrameSlimPreview() {
    PS2Theme {
        FrameSlim {
            BoldButton(label = "Android")
        }
    }
}
