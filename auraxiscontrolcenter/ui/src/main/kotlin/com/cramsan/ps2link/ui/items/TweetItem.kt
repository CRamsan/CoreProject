package com.cramsan.ps2link.ui.items

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.ui.SlimButton
import com.cramsan.ps2link.ui.theme.PS2Theme

@Composable
fun TweetItem(
    modifier: Modifier = Modifier,
    username: String? = null,
) {
    SlimButton(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            username?.let {
                Text(
                    text = it,
                )
            }
        }
    }
}

@Preview
@Composable
fun TweetItemPreview() {
    PS2Theme {
        TweetItem(
            username = "Planetside",
        )
    }
}
