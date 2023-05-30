package com.cramsan.ps2link.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cramsan.ps2link.ui.theme.Opacity
import com.cramsan.ps2link.ui.widgets.UnexpectedError

@Composable
fun LoadingOverlay(
    modifier: Modifier = Modifier,
    enabled: Boolean = false,
) {
    AnimatedVisibility(
        visible = enabled,
        enter = fadeIn(),
        exit = fadeOut(),
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

/**
 * Display a semi-translucent overlay with [resourceId] as an error message.
 */
@Composable
@Suppress("FunctionNaming")
fun ErrorOverlay(
    modifier: Modifier = Modifier.fillMaxSize(),
    isError: Boolean = false,
    error: String,
) {
    AnimatedVisibility(
        visible = isError,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Surface(
            modifier,
            color = MaterialTheme.colors.primary.setAlpha(Opacity.transparent),
        ) {
            Box(modifier.fillMaxSize()) {
                UnexpectedError(modifier = Modifier.align(Alignment.Center), error)
            }
        }
    }
}
