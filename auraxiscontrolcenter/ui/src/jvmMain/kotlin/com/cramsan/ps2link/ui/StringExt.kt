package com.cramsan.ps2link.ui

import androidx.compose.runtime.Composable
import com.cramsan.ps2link.core.models.KillType
import com.cramsan.ps2link.core.models.LoginStatus
import com.cramsan.ps2link.core.models.Population
import com.cramsan.ps2link.core.models.ServerStatus

@Composable
actual fun unknownString(): String {
    return "Unknown"
}

@Composable
internal actual fun KillType.toText(): String {
    return when (this) {
        KillType.KILL -> "KILL"
        KillType.KILLEDBY -> "KILLED BY"
        KillType.SUICIDE -> "SUICIDE"
        KillType.UNKNOWN -> "UNKNOWN"
    }
}

@Composable
actual fun LoginStatus.toText(): String {
    return when (this) {
        LoginStatus.ONLINE -> "ONLINE"
        LoginStatus.OFFLINE -> "OFFLINE"
        LoginStatus.UNKNOWN -> "UNKNOWN"
    }
}

@Composable
actual fun ServerStatus.toText(): String {
    return when (this) {
        ServerStatus.ONLINE -> "ONLINE"
        ServerStatus.OFFLINE -> "OFFLINE"
        ServerStatus.LOCKED -> "LOCKED"
        ServerStatus.UNKNOWN -> "UNKNOWN"
    }
}

@Composable
actual fun Population.toText(): String {
    return when (this) {
        Population.HIGH -> "HIGH"
        Population.MEDIUM -> "MEDIUM"
        Population.LOW -> "LOW"
        Population.UNKNOWN -> "UNKNOWN"
    }
}
