package com.cramsan.ps2link.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.ui.theme.Opacity
import com.cramsan.ps2link.ui.theme.PS2Theme
import com.cramsan.ps2link.ui.theme.Padding
import com.cramsan.ps2link.ui.theme.Size

data class BoldButtonColors(private val backgroundColor: Color, private val contentColor: Color) :
    ButtonColors {

    @Composable
    override fun backgroundColor(enabled: Boolean): State<Color> {
        return rememberUpdatedState(backgroundColor)
    }

    @Composable
    override fun contentColor(enabled: Boolean): State<Color> {
        return rememberUpdatedState(contentColor)
    }
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
            // TODO: Add content description
            if (star) {
                Image(
                    painter = painterResource(id = R.drawable.icon_star_selected),
                    contentDescription = null
                )
            }
            Text(
                text = label,
                style = MaterialTheme.typography.h5,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(vertical = Padding.small)
                    .fillMaxWidth()
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
        contentColor = contentColorFor(color)
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

@Preview(
    showBackground = true,
    backgroundColor = 0xFF000000,
)
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
