package com.cramsan.ps2link.network.ws.testgui.ui.lib

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cramsan.ps2link.network.ws.testgui.ui.lib.theme.Opacity
import com.cramsan.ps2link.network.ws.testgui.ui.lib.widgets.UnexpectedError

@Composable
fun LoadingOverlay(
    modifier: Modifier = Modifier.fillMaxSize(),
    enabled: Boolean = false,
) {
    AnimatedVisibility(
        visible = enabled,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Box(
            modifier = modifier.background(MaterialTheme.colors.primary.setAlpha(Opacity.transparent)),
        ) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
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
    errorMessage: String? = null,
) {
    AnimatedVisibility(
        visible = isError,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Box(
            modifier = modifier.background(MaterialTheme.colors.primary.setAlpha(Opacity.transparent)),
        ) {
            if (errorMessage == null) {
                UnexpectedError(modifier = Modifier.align(Alignment.Center))
            } else {
                UnexpectedError(modifier = Modifier.align(Alignment.Center), errorMessage)
            }
        }
    }
}
