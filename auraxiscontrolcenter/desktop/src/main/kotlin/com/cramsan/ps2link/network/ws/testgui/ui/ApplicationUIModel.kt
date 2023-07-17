package com.cramsan.ps2link.network.ws.testgui.ui

import com.cramsan.ps2link.network.ws.testgui.application.ProgramMode
import com.cramsan.ps2link.network.ws.testgui.ui.dialogs.PS2DialogType
import com.cramsan.ps2link.network.ws.testgui.ui.tabs.ApplicationTabUIModel

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
        val selectedTab: ApplicationTabUIModel,
        val profileTab: ApplicationTabUIModel.Profile,
        val outfitTab: ApplicationTabUIModel.Outfit,
        val trackerTab: ApplicationTabUIModel.Tracker,
    )

    data class DialogUIModel(
        val dialogType: PS2DialogType,
    )
}
