package com.cramsan.ps2link.network.ws.testgui.ui

import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.network.ws.testgui.application.ProgramMode
import com.cramsan.ps2link.network.ws.testgui.ui.navigation.ScreenType

/**
 * Data class that holds the UI state for the entire application.
 */
data class ApplicationUIModel(
    val isWindowOpen: Boolean,
    val screenType: ScreenType,
    val status: String,
    val actionLabel: String?,
    val trayIconPath: String,
    val windowIconPath: String,
    val programMode: ProgramMode,
    val character: Character?,
    val debugModeEnabled: Boolean,
)
