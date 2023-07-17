package com.cramsan.ps2link.network.ws.testgui.ui.screens.tracker

import com.cramsan.ps2link.network.ws.testgui.application.ProgramMode

data class TrackerUIModel(
    val mode: ProgramMode,
    val profileName: String?,
    val events: List<PlayerEventUIModel>,
)
