package com.cramsan.ps2link.ui.widgets

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.cramsan.ps2link.ui.FrameSlim
import com.cramsan.ps2link.ui.theme.Padding
import com.cramsan.ps2link.ui.theme.errorColor

@Composable
fun UnexpectedError(
    modifier: Modifier = Modifier,
    message: String,
) {
    FrameSlim(modifier) {
        Text(
            text = message,
            modifier = Modifier.padding(Padding.medium),
            color = errorColor,
            textAlign = TextAlign.Center,
        )
    }
}


