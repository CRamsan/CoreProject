package com.cramsan.ps2link.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource

/**
 * @Author cramsan
 * @created 1/17/2021
 */

fun Color.setAlpha(alpha: Float) = this.copy(alpha = alpha)

@Composable
fun OnlineStatus.toStringResource() = when (this) {
    OnlineStatus.ONLINE -> stringResource(R.string.text_online)
    OnlineStatus.OFFLINE -> stringResource(R.string.text_offline)
    OnlineStatus.UNKNOWN -> stringResource(R.string.text_unknown)
}
