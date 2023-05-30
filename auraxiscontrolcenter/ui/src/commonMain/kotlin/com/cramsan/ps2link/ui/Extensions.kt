package com.cramsan.ps2link.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.cramsan.ps2link.core.models.KillType
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

fun LoginStatus?.toColor() = when (this) {
    LoginStatus.ONLINE -> positive
    LoginStatus.OFFLINE -> negative
    LoginStatus.UNKNOWN, null -> undefined
}

fun ServerStatus?.toColor() = when (this) {
    ServerStatus.ONLINE -> positive
    ServerStatus.OFFLINE -> negative
    ServerStatus.LOCKED -> warning
    ServerStatus.UNKNOWN, null -> undefined
}

fun KillType?.toColor() = when (this) {
    KillType.KILL -> positive
    KillType.KILLEDBY, KillType.SUICIDE, KillType.UNKNOWN, null -> negative
}

@Composable
fun Population.toColor() = when (this) {
    Population.HIGH -> positive
    Population.MEDIUM -> warning
    Population.LOW -> warning
    Population.UNKNOWN -> undefined
}
