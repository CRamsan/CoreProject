package com.cramsan.ps2link.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.cramsan.ps2link.core.models.LoginStatus
import com.cramsan.ps2link.core.models.Population
import com.cramsan.ps2link.core.models.ServerStatus
import com.cramsan.ps2link.ui.theme.negative
import com.cramsan.ps2link.ui.theme.positive
import com.cramsan.ps2link.ui.theme.undefined
import com.cramsan.ps2link.ui.theme.warning

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

@Composable
fun ServerStatus.toStringResource() = when (this) {
    ServerStatus.ONLINE -> stringResource(R.string.text_online_caps)
    ServerStatus.OFFLINE -> stringResource(R.string.text_offline_caps)
    ServerStatus.LOCKED -> stringResource(R.string.text_locked_caps)
    ServerStatus.UNKNOWN -> stringResource(R.string.text_unknown_caps)
}

fun ServerStatus.toColor() = when (this) {
    ServerStatus.ONLINE -> positive
    ServerStatus.OFFLINE -> negative
    ServerStatus.LOCKED -> warning
    ServerStatus.UNKNOWN -> undefined
}

@Composable
fun Population.toStringResource(): String {
    val argument = when (this) {
        Population.HIGH -> stringResource(R.string.text_high_caps)
        Population.MEDIUM -> stringResource(R.string.text_medium_caps)
        Population.LOW -> stringResource(R.string.text_low_caps)
        Population.UNKNOWN -> stringResource(R.string.text_unknown_caps)
    }

    return stringResource(R.string.text_server_population, argument)
}

@Composable
fun Population.toColor() = when (this) {
    Population.HIGH -> positive
    Population.MEDIUM -> warning
    Population.LOW -> warning
    Population.UNKNOWN -> undefined
}
