package com.cramsan.ps2link.network.ws.testgui.ui

import com.cramsan.ps2link.network.ws.testgui.application.ProgramMode
import com.cramsan.ps2link.network.ws.testgui.ui.dialogs.PS2DialogType
import com.cramsan.ps2link.network.ws.testgui.ui.tabs.ApplicationTab

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
        val dialogUIModel: DialogUIModel?,
        val showFTE: Boolean,
        val title: String,
        val showAddButton: Boolean,
    )

    data class TrayUIModel(
        val statusLabel: String,
        val actionLabel: String?,
        val iconPath: String,
    )

    data class State(
        val programMode: ProgramMode,
        val debugModeEnabled: Boolean,
        val selectedTab: ApplicationTab,
        val profileTab: ApplicationTab.Profile,
        val outfitTab: ApplicationTab.Outfit,
        val trackerTab: ApplicationTab.Tracker,
    )

    data class DialogUIModel(
        val dialogType: PS2DialogType,
    )
}
