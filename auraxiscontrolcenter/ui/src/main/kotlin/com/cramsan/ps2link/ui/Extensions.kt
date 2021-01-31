package com.cramsan.ps2link.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.cramsan.ps2link.core.models.LoginStatus

/**
 * @Author cramsan
 * @created 1/17/2021
 */

fun Color.setAlpha(alpha: Float) = this.copy(alpha = alpha)

@Composable
fun LoginStatus.toStringResource() = when (this) {
    LoginStatus.ONLINE -> stringResource(R.string.text_online)
    LoginStatus.OFFLINE -> stringResource(R.string.text_offline)
    LoginStatus.UNKNOWN -> stringResource(R.string.text_unknown)
}
