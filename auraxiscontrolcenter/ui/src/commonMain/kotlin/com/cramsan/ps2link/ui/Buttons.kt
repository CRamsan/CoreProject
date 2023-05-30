package com.cramsan.ps2link.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
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
import androidx.compose.ui.graphics.painter.Painter
import com.cramsan.ps2link.ui.theme.Opacity
import com.cramsan.ps2link.ui.theme.Padding
import com.cramsan.ps2link.ui.theme.Size
import com.cramsan.ps2link.ui.widgets.CustomCircularProgressIndicator

internal data class BoldButtonColors(private val backgroundColor: Color, private val contentColor: Color) :
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
    content: @Composable () -> Unit,
) {
    val color = MaterialTheme.colors.primary
    val buttonColors = BoldButtonColors(
        backgroundColor = color.setAlpha(
            Opacity.translucent,
        ),
        contentColor = MaterialTheme.colors.onPrimary,
    )

    Button(
        onClick = onClick,
        enabled = enabled,
        border = BorderStroke(Size.xsmall, color),
        colors = buttonColors,
        modifier = modifier.defaultMinSize(minHeight = Size.xxlarge),
    ) {
        content()
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainMenuButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = null,
    star: Boolean = false,
    onClick: () -> Unit = {},
) {
    BoldButton(
        modifier = modifier,
        enabled = enabled,
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier.animateContentSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // TODO: Add content description
            if (star) {
                Image(
                    painter = StarPainter(),
                    contentDescription = null,
                )
            }
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                val isLoading = label == null
                Crossfade(targetState = isLoading) {
                    if (it) {
                        CustomCircularProgressIndicator(
                            color = MaterialTheme.colors.onPrimary,
                            diameter = Size.large,
                        )
                    } else {
                        Text(
                            text = label ?: "",
                            style = MaterialTheme.typography.h5,
                            modifier = Modifier
                                .padding(vertical = Padding.small),
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal expect fun StarPainter(): Painter

@Composable
fun SlimButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    val color = MaterialTheme.colors.primary
    val buttonColors = BoldButtonColors(
        backgroundColor = color.setAlpha(Opacity.transparent),
        contentColor = contentColorFor(color),
    )

    Button(
        onClick = onClick,
        border = BorderStroke(Size.xmicro, color),
        enabled = enabled,
        colors = buttonColors,
        modifier = modifier.defaultMinSize(minHeight = Size.xxlarge),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            content()
        }
    }
}
