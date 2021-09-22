package com.cramsan.ps2link.ui.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.ui.theme.PS2Theme
import com.cramsan.ps2link.ui.theme.Padding
import com.cramsan.ps2link.ui.theme.Size
import com.cramsan.ps2link.ui.theme.gold
import com.cramsan.ps2link.ui.theme.goldBackground
import com.cramsan.ps2link.ui.theme.goldDisabled
import com.cramsan.ps2link.ui.theme.goldWhite

@Composable
fun BR(
    modifier: Modifier = Modifier,
    level: Int,
    enabled: Boolean = true,
) {
    val shape = MaterialTheme.shapes.small
    val backgroundColor = if (enabled) {
        Color.White
    } else {
        goldDisabled
    }
    val borderColor = if (enabled) {
        gold
    } else {
        goldDisabled
    }
    Text(
        text = level.toString(),
        modifier = modifier
            .background(backgroundColor, shape)
            .clip(shape)
            .border(BorderStroke(Size.micro, borderColor), shape)
            .padding(Padding.small),
        color = Color.Black,
        textAlign = TextAlign.Center,
    )
}

@Composable
fun BRBar(
    modifier: Modifier = Modifier,
    percentageToNextLevel: Float,
) {
    val height = Size.large
    Row(
        modifier = modifier
            .height(height)
            .fillMaxWidth()
            .padding(Padding.xsmall)
            .clip(CutCornerShape(Size.xsmall))
            .background(BRGradient)
            .border(width = Size.micro, color = goldBackground, shape = CutCornerShape(Size.xsmall)),
    ) {
        if (percentageToNextLevel > 0) {
            Spacer(
                modifier = Modifier
                    .weight(percentageToNextLevel)
            )
            Box(
                modifier = Modifier
                    .weight(100 - percentageToNextLevel)
                    .fillMaxHeight()
                    .background(goldBackground)
            )
        }
    }
}

private val BRGradient = Brush.horizontalGradient(listOf(goldWhite, gold))

@Preview(
    widthDp = 200,
    heightDp = 25,
)
@Composable
fun ProfileItemPreview() {
    PS2Theme {
        Row(modifier = Modifier.fillMaxWidth()) {
            BR(
                level = 80,
                enabled = true,
            )
            BRBar(percentageToNextLevel = 75f)
            BR(
                level = 181,
                enabled = false,
            )
        }
    }
}
