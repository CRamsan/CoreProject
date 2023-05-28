package com.cramsan.ps2link.network.ws.testgui.ui

import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.network.ws.testgui.application.ProgramMode
import com.cramsan.ps2link.network.ws.testgui.ui.navigation.ScreenType

/**
 * Data class that holds the UI state for the entire application.
 */
data class ApplicationUIModel(
    val windowUIModel: WindowUIModel,
    val trayUIModel: TrayUIModel,
    val state: State,
) {

    data class WindowUIModel(
        val isVisible: Boolean,
        val iconPath: String,
    )

    data class TrayUIModel(
        val statusLabel: String,
        val actionLabel: String?,
        val iconPath: String,
    )

    data class State(
        val screenType: ScreenType,
        val programMode: ProgramMode,
        val character: Character?,
        val debugModeEnabled: Boolean,
    )
}
