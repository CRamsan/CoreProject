package com.cramsan.ps2link.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cramsan.ps2link.ui.theme.PS2Theme
import com.cramsan.ps2link.ui.theme.teal200
import com.cramsan.ps2link.ui.theme.teal200Alph
import com.cramsan.ps2link.ui.theme.teal900

@Composable
fun Button(modifier: Modifier = Modifier, label: String, onClick: () -> Unit = {}) {
    val shape = MaterialTheme.shapes.small
    Text(
        text = label,
        modifier = modifier.background(teal200Alph, shape)
            .clickable(onClick = onClick)
            .clip(shape)
            .border(BorderStroke(1.dp, teal900), shape)
            .padding(
                horizontal = 10.dp,
                vertical = 5.dp
            ),
        color = MaterialTheme.colors.onPrimary,
        textAlign = TextAlign.Center,
    )
}

@Composable
fun BoldButton(modifier: Modifier = Modifier, label: String, star: Boolean = false, onClick: () -> Unit = {}) {
    val shape = MaterialTheme.shapes.small
    Row(
        modifier = modifier.background(teal200, shape)
            .clickable(onClick = onClick)
            .clip(shape)
            .border(BorderStroke(3.dp, teal900), shape)
            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (star) {
            Image(imageResource(id = R.drawable.icon_star_selected))
        }
        Text(
            text = label,
            color = MaterialTheme.colors.onPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(
    name = "Bold Button",
    group = "Bold"
)
@Composable
fun BoldButtonPreview() {
    PS2Theme {
        BoldButton(label = "Android")
    }
}

@Preview(
    name = "Normal Button",
    group = "Normal"
)
@Composable
fun NormalButtonPreview() {
    PS2Theme {
        Button(label = "Android")
    }
}
