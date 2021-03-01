package com.cramsan.ps2link.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.ui.theme.Opacity
import com.cramsan.ps2link.ui.theme.PS2Theme

@OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)
@Composable
fun LoadingOverlay(
    modifier: Modifier = Modifier,
    enabled: Boolean = false,
) {
    AnimatedVisibility(
        visible = enabled,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Surface(
            modifier.fillMaxSize(),
            color = MaterialTheme.colors.primary.setAlpha(Opacity.transparent),
        ) {
            Box(modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF000000,
)
@Composable
fun LoadingOverlayPreview() {
    PS2Theme {
        Column {
            SearchField(value = "cramsan", hint = "Player name", onValueChange = { /*TODO*/ })
            LoadingOverlay()
        }
    }
}
