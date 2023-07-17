package com.cramsan.ps2link.ui.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.cramsan.ps2link.ui.FrameSlim
import com.cramsan.ps2link.ui.theme.Padding
import com.cramsan.ps2link.ui.theme.errorColor

@Composable
fun UnexpectedError(
    modifier: Modifier = Modifier,
    message: String,
    onRefreshRequested: (() -> Unit)? = null,
) {
    FrameSlim(modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = message,
                modifier = Modifier.padding(Padding.medium),
                color = errorColor,
                textAlign = TextAlign.Center,
            )

            onRefreshRequested?.let {
                RetryButton(
                    modifier = Modifier.padding(Padding.large),
                    it,
                )
            }
        }
    }
}

@Composable
expect fun RetryButton(modifier: Modifier = Modifier, onRefreshRequested: () -> Unit)
