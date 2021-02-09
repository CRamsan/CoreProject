package com.cramsan.ps2link.ui.items

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.ui.SlimButton
import com.cramsan.ps2link.ui.theme.PS2Theme

@Composable
fun RedditPostItem(
    modifier: Modifier = Modifier,
    label: String,
    onClick: () -> Unit = {},
) {
    SlimButton(
        modifier = modifier
    ) {
        Column {
            Text(
                text = label,
            )
        }
    }
}

@Preview
@Composable
fun RedditPostItemPreview() {
    PS2Theme {
        RedditPostItem(
            label = "Sticky",
        )
    }
}
