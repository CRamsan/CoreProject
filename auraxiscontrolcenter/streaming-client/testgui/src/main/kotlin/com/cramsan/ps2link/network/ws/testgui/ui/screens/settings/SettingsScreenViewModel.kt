package com.cramsan.ps2link.network.ws.testgui.ui.screens.settings

import androidx.compose.runtime.Stable
import com.cramsan.ps2link.network.ws.testgui.application.ApplicationManager
import com.cramsan.ps2link.network.ws.testgui.hoykeys.HotKeyEvent
import com.cramsan.ps2link.network.ws.testgui.hoykeys.HotKeyManager
import com.cramsan.ps2link.network.ws.testgui.hoykeys.HotKeyManagerEventListener
import com.cramsan.ps2link.network.ws.testgui.hoykeys.HotKeyType
import com.cramsan.ps2link.network.ws.testgui.hoykeys.KotlinKeyEvent
import com.cramsan.ps2link.network.ws.testgui.ui.navigation.ScreenType
import com.cramsan.ps2link.network.ws.testgui.ui.screens.BaseViewModel
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel for managing the Settings screen.
 */
@Stable
class SettingsScreenViewModel(
    applicationManager: ApplicationManager,
    private val hotKeyManager: HotKeyManager,
) : BaseViewModel(applicationManager) {

    private val _uiState = MutableStateFlow(
        SettingUIState(
            false,
            persistentListOf(),
            false,
        ),
    )
    val uiState = _uiState.asStateFlow()

    private val hotKeyManagerEventListener = object : HotKeyManagerEventListener {
        override fun onKeyEvent(hotKeyType: HotKeyType, keyEvent: KotlinKeyEvent) = Unit

        override fun onCaptureComplete() {
            updateUI()
            _uiState.value = _uiState.value.copy(
                capturing = false,
            )
        }
    }

    override fun onStart() {
        hotKeyManager.registerListener(hotKeyManagerEventListener)
        updateUI()
    }

    private fun updateUI() {
        val list = hotKeyManager.loadFromPreferences().map {
            SettingUIState.HotKeyState(
                it.key.toLabel(),
                it.value.toKeyCombinationLabel(),
                it.key,
                it.value,
            )
        }

        _uiState.value = _uiState.value.copy(
            isEnabled = true,
            list = list.toImmutableList(),
        )
    }

    override fun onClose() {
        hotKeyManager.deregisterListener(hotKeyManagerEventListener)
    }

    /**
     * Start listening for user key events.
     */
    fun captureHotKeys(hotKeyType: HotKeyType) {
        _uiState.value = _uiState.value.copy(
            capturing = true,
        )
        hotKeyManager.initiateHotKeyRegistration(hotKeyType)
    }

    fun stopCapture() {
        hotKeyManager.stopHotKeyRegistration()
    }

    /**
     * Close the current screen and navigate to the main screen.
     */
    fun returnToMainScreen() {
        applicationManager.setCurrentScreen(ScreenType.MAIN)
    }

    companion object {

        private fun HotKeyEvent?.toKeyCombinationLabel(): String {
            if (this == null) {
                return ""
            }

            val pressEvents = kotlinEvents.filter { it.id == NativeKeyEvent.NATIVE_KEY_PRESSED }
            return pressEvents.joinToString(" + ") {
                NativeKeyEvent.getKeyText(it.keyCode)
            }
        }

        private fun HotKeyType.toLabel(): String = when (this) {
            HotKeyType.ON_ENEMY_KILL_SCREENSHOT -> "On Enemy Kill - ScreenShot"
            HotKeyType.ON_ENEMY_KILL_VIDEO -> "On Enemy Kill - Video Capture"
        }
    }
}
