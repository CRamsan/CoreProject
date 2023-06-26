package com.cramsan.ps2link.network.ws.testgui.application

fun pathForStatus(programMode: ProgramMode) = when (programMode) {
    ProgramMode.NOT_CONFIGURED, ProgramMode.LOADING -> "icon_small.png"
    ProgramMode.RUNNING -> "icon_running.png"
    ProgramMode.PAUSED -> "icon_not_running.png"
}

fun ProgramMode.toFriendlyString(): String {
    return when (this) {
        ProgramMode.NOT_CONFIGURED, ProgramMode.LOADING -> "Not configured"
        ProgramMode.RUNNING -> "Running"
        ProgramMode.PAUSED -> "Not Running"
    }
}

fun ProgramMode.toActionLabel(): String? {
    return when (this) {
        ProgramMode.NOT_CONFIGURED, ProgramMode.LOADING -> null
        ProgramMode.RUNNING -> "Pause"
        ProgramMode.PAUSED -> "Start"
    }
}
