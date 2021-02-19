package com.cramsan.ps2link.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.ui.theme.Opacity
import com.cramsan.ps2link.ui.theme.PS2Theme
import com.cramsan.ps2link.ui.theme.Padding
import com.cramsan.ps2link.ui.theme.Size

@OptIn(ExperimentalMaterialApi::class)
data class BoldButtonColors(private val backgroundColor: Color, private val contentColor: Color) :
    ButtonColors {
    override fun backgroundColor(enabled: Boolean): Color = backgroundColor
    override fun contentColor(enabled: Boolean): Color = contentColor
}

@Composable
fun BoldButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val color = MaterialTheme.colors.primary
    val buttonColors = BoldButtonColors(
        backgroundColor = color.setAlpha(
            Opacity.translucent
        ),
        contentColor = MaterialTheme.colors.onPrimary
    )

    Button(
        onClick = onClick,
        enabled = enabled,
        border = BorderStroke(Size.xsmall, color),
        colors = buttonColors,
        modifier = modifier,
    ) {
        content()
    }
}

@Composable
fun MainMenuButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String,
    star: Boolean = false,
    onClick: () -> Unit = {},
) {
    BoldButton(
        modifier = modifier,
        enabled = enabled,
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (star) {
                Image(imageResource(id = R.drawable.icon_star_selected))
            }
            Text(
                text = label,
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = Padding.large).fillMaxWidth()
            )
        }
    }
}

@Composable
fun SlimButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val color = MaterialTheme.colors.primary
    val buttonColors = BoldButtonColors(
        backgroundColor = color.setAlpha(Opacity.transparent),
        contentColor = contentColorFor(color = color)
    )

    Button(
        onClick = onClick,
        border = BorderStroke(Size.xmicro, color),
        enabled = enabled,
        colors = buttonColors,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            content()
        }
    }
}

@Preview
@Composable
fun BoldButtonPreview() {
    PS2Theme {
        Column {
            BoldButton {
                Text("Android")
            }
            MainMenuButton(label = "Crasman2", star = true)
            BoldButton {
                Text("Test")
            }
            SlimButton {
                Text("Slim Test")
            }
        }
    }
}
