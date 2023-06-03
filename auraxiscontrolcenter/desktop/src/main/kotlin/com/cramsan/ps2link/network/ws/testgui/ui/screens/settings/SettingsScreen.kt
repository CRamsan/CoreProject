package com.cramsan.ps2link.network.ws.testgui.ui.screens.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.ps2link.network.ws.testgui.application.ApplicationManager
import com.cramsan.ps2link.network.ws.testgui.hoykeys.HotKeyManager
import com.cramsan.ps2link.network.ws.testgui.hoykeys.HotKeyType
import com.cramsan.ps2link.network.ws.testgui.ui.screens.BaseScreen
import com.cramsan.ps2link.ui.BoldButton
import com.cramsan.ps2link.ui.FrameSlim
import com.cramsan.ps2link.ui.SlimButton
import com.cramsan.ps2link.ui.setAlpha
import com.cramsan.ps2link.ui.theme.Opacity
import com.cramsan.ps2link.ui.theme.Padding
import com.cramsan.ps2link.ui.theme.Size
import kotlinx.collections.immutable.ImmutableList

/**
 * Screen for displaying and managing settings.
 */
class SettingsScreen(
    private val applicationManager: ApplicationManager,
    private val hotKeyManager: HotKeyManager,
    private val dispatcherProvider: DispatcherProvider,
) : BaseScreen<SettingsScreenViewModel>() {

    override fun createViewModel(): SettingsScreenViewModel {
        return SettingsScreenViewModel(applicationManager, dispatcherProvider, hotKeyManager)
    }

    @Composable
    override fun ScreenContent(viewModel: SettingsScreenViewModel) {
        SettingsContent(viewModel)
    }
}

@Suppress("LongMethod")
@Composable
private fun SettingsContent(viewModel: SettingsScreenViewModel) {
    val settingsUIState by viewModel.uiState.collectAsState()
    val onHotKeyButtonPressed = { hotKeyType: HotKeyType ->
        viewModel.captureHotKeys(hotKeyType)
    }
    FrameSlim(
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            FrameSlim(
                modifier = Modifier
                    .padding(Padding.medium)
                    .fillMaxHeight(),
            ) {
                Column(
                    modifier = Modifier.padding(Padding.medium),
                ) {
                    SettingsHotKeySection(
                        settingsUIState.list,
                        onHotKeyButtonPressed,
                    )
                    SettingsDebugSection(
                        settingsUIState.isDebugEnabled,
                        onDebugEnabledChanged = { isChecked -> viewModel.changeDebugMode(isChecked) },
                        onOpenLogFolder = { viewModel.openLogFolder() },
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    BoldButton(
                        modifier = Modifier.align(Alignment.End),
                        onClick = { viewModel.returnToMainScreen() },
                    ) {
                        Text("Back")
                    }
                }
            }
            DialogOverlay(
                isVisible = settingsUIState.capturing,
                onCancel = { viewModel.stopCapture() },
            )

            RestartDialogOverlay(
                isVisible = settingsUIState.restartDialog,
                onCancel = { viewModel.closeDialog() },
            )
        }
    }
}

@Composable
private fun SettingsHotKeySection(
    settingsList: ImmutableList<SettingUIState.HotKeyState>,
    onHotKeyButtonPressed: (HotKeyType) -> Unit,
) {
    Column(
        modifier = Modifier.wrapContentSize().padding(vertical = Padding.medium),
    ) {
        Text(
            "Keyboard Shortcuts",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier,
        )
        Column {
            settingsList.forEach {
                Row(
                    modifier = Modifier.padding(Padding.small),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    BoldButton(
                        modifier = Modifier.width(150.dp),
                        onClick = { onHotKeyButtonPressed(it.hotKeyType) },
                    ) {
                        Text(
                            it.keyCombinationLabel,
                            modifier = Modifier,
                        )
                    }
                    Spacer(Modifier.width(Size.small))
                    Text(
                        it.label,
                        modifier = Modifier,
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsDebugSection(
    isDebugEnabled: Boolean,
    onDebugEnabledChanged: (Boolean) -> Unit,
    onOpenLogFolder: () -> Unit,
) {
    Column(
        modifier = Modifier.wrapContentSize().padding(vertical = Padding.medium),
    ) {
        Text(
            "Debug",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier,
        )
        Column {
            Row(
                modifier = Modifier.padding(Padding.small),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = isDebugEnabled,
                    onCheckedChange = { onDebugEnabledChanged(it) },
                )
                Text(
                    "Enable Debug mode",
                    modifier = Modifier,
                )
            }
            Row(
                modifier = Modifier.padding(Padding.small),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BoldButton(
                    modifier = Modifier.width(150.dp),
                    onClick = { onOpenLogFolder() },
                ) {
                    Text(
                        "Open",
                        modifier = Modifier,
                    )
                }
                Spacer(Modifier.width(Size.small))
                Text(
                    "Log files",
                    modifier = Modifier,
                )
            }
        }
    }
}

@Composable
private fun DialogOverlay(
    isVisible: Boolean = false,
    onCancel: () -> Unit,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.setAlpha(Opacity.translucent)),
        ) {
            FrameSlim(
                modifier = Modifier.align(Alignment.Center).padding(Padding.medium),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        "Capturing hot key",
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier.padding(Padding.medium),
                    )
                    Text(
                        "Press any key combination.",
                        modifier = Modifier.padding(Padding.medium),
                    )
                    SlimButton(
                        modifier = Modifier.padding(Padding.medium),
                        onClick = { onCancel() },
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}

@Composable
private fun RestartDialogOverlay(
    isVisible: Boolean = false,
    onCancel: () -> Unit,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.setAlpha(Opacity.translucent)),
        ) {
            FrameSlim(
                modifier = Modifier.align(Alignment.Center).padding(Padding.medium),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        "Restart to apply changes",
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier.padding(Padding.medium),
                    )
                    SlimButton(
                        modifier = Modifier.padding(Padding.medium),
                        onClick = { onCancel() },
                    ) {
                        Text("Ok")
                    }
                }
            }
        }
    }
}
