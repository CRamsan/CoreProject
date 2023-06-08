package com.cramsan.ps2link.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.cramsan.ps2link.core.models.KillType
import com.cramsan.ps2link.core.models.LoginStatus
import com.cramsan.ps2link.core.models.Population
import com.cramsan.ps2link.core.models.ServerStatus

@Composable
internal actual fun KillType.toText(): String {
    val resource = when (this) {
        KillType.KILL -> R.string.text_killed_caps
        KillType.KILLEDBY -> R.string.text_killed_by_caps
        KillType.SUICIDE -> R.string.text_suicide_caps
        KillType.UNKNOWN -> R.string.title_unkown
    }
    return stringResource(resource)
}

@Composable
actual fun unknownString(): String {
    return stringResource(R.string.text_unknown)
}

@Composable
actual fun LoginStatus.toText(): String = when (this) {
    LoginStatus.ONLINE -> stringResource(R.string.text_online)
    LoginStatus.OFFLINE -> stringResource(R.string.text_offline)
    LoginStatus.UNKNOWN -> stringResource(R.string.text_unknown)
}

@Composable
actual fun ServerStatus.toText(): String = when (this) {
    ServerStatus.ONLINE -> stringResource(R.string.text_online_caps)
    ServerStatus.OFFLINE -> stringResource(R.string.text_offline_caps)
    ServerStatus.LOCKED -> stringResource(R.string.text_locked_caps)
    ServerStatus.UNKNOWN -> stringResource(R.string.text_unknown_caps)
}

@Composable
actual fun Population.toText(): String {
    val argument = when (this) {
        Population.HIGH -> stringResource(R.string.text_high_caps)
        Population.MEDIUM -> stringResource(R.string.text_medium_caps)
        Population.LOW -> stringResource(R.string.text_low_caps)
        Population.UNKNOWN -> stringResource(R.string.text_unknown_caps)
    }

    return stringResource(R.string.text_server_population, argument)
}
