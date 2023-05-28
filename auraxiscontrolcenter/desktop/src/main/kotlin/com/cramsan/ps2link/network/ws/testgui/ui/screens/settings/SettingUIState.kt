package com.cramsan.ps2link.network.ws.testgui.ui.screens.settings

import com.cramsan.ps2link.network.ws.testgui.hoykeys.HotKeyEvent
import com.cramsan.ps2link.network.ws.testgui.hoykeys.HotKeyType
import kotlinx.collections.immutable.ImmutableList

/**
 * Data class that represents the UI state of the Settings screen.
 */
data class SettingUIState(
    val isEnabled: Boolean,
    val list: ImmutableList<HotKeyState>,
    val capturing: Boolean,
    val isDebugEnabled: Boolean,
    val restartDialog: Boolean,
) {

    /**
     * Data class that represents a HotKey UI element
     */
    data class HotKeyState(
        val label: String,
        val keyCombinationLabel: String,
        val hotKeyType: HotKeyType,
        val hotKeyEvent: HotKeyEvent?,
    )
}
