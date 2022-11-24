package com.cramsan.ps2link.network.ws.testgui.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.cramsan.ps2link.network.ws.testgui.application.ApplicationManager
import com.cramsan.ps2link.network.ws.testgui.hoykeys.HotKeyManager
import com.cramsan.ps2link.network.ws.testgui.hoykeys.HotKeyType
import com.cramsan.ps2link.network.ws.testgui.ui.screens.BaseScreen

/**
 * Screen for displaying and managing settings.
 */
class SettingsScreen(
    private val applicationManager: ApplicationManager,
    private val hotKeyManager: HotKeyManager,
) : BaseScreen<SettingsScreenViewModel>() {

    override fun createViewModel(): SettingsScreenViewModel {
        return SettingsScreenViewModel(applicationManager, hotKeyManager)
    }

    @Composable
    override fun ScreenContent(viewModel: SettingsScreenViewModel) {
        SettingsContent(viewModel)
    }
}

@Composable
private fun SettingsContent(viewModel: SettingsScreenViewModel) {
    val settingsUIState by viewModel.uiState.collectAsState()
    val onHotKeyButtonPressed = { hotKeyType: HotKeyType ->
        viewModel.captureHotKeys(hotKeyType)
    }

    Column {
        Text(
            "Keyboard Shortcuts",
            modifier = Modifier,
        )
        LazyColumn(
            modifier = Modifier,
        ) {
            items(settingsUIState.list) {
                Button(
                    enabled = settingsUIState.isEnabled,
                    onClick = { onHotKeyButtonPressed(it.hotKeyType) },
                ) {
                    Text(
                        it.keyCombinationLabel,
                        modifier = Modifier,
                    )
                }
                Text(
                    it.label,
                    modifier = Modifier,
                )
            }
        }
        Row {
            Button(
                onClick = { viewModel.returnToMainScreen() },
            ) {
                Text("Back")
            }
        }
    }
}
