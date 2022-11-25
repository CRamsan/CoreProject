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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cramsan.ps2link.network.ws.testgui.application.ApplicationManager
import com.cramsan.ps2link.network.ws.testgui.hoykeys.HotKeyManager
import com.cramsan.ps2link.network.ws.testgui.hoykeys.HotKeyType
import com.cramsan.ps2link.network.ws.testgui.ui.lib.BoldButton
import com.cramsan.ps2link.network.ws.testgui.ui.lib.FrameSlim
import com.cramsan.ps2link.network.ws.testgui.ui.lib.SlimButton
import com.cramsan.ps2link.network.ws.testgui.ui.lib.setAlpha
import com.cramsan.ps2link.network.ws.testgui.ui.lib.theme.FontSize
import com.cramsan.ps2link.network.ws.testgui.ui.lib.theme.Opacity
import com.cramsan.ps2link.network.ws.testgui.ui.lib.theme.Padding
import com.cramsan.ps2link.network.ws.testgui.ui.lib.theme.ScreenSizes
import com.cramsan.ps2link.network.ws.testgui.ui.lib.theme.Size
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

@Suppress("LongMethod")
@Composable
private fun SettingsContent(viewModel: SettingsScreenViewModel) {
    val settingsUIState by viewModel.uiState.collectAsState()
    val onHotKeyButtonPressed = { hotKeyType: HotKeyType ->
        viewModel.captureHotKeys(hotKeyType)
    }
    FrameSlim(
        modifier = Modifier
            .width(ScreenSizes.maxColumnWidth)
            .fillMaxHeight(),
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
                    Column(
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(
                            "Keyboard Shortcuts",
                            fontSize = FontSize.subtitle,
                            modifier = Modifier,
                        )
                        Column {
                            settingsUIState.list.forEach {
                                Row(
                                    modifier = Modifier.padding(Padding.small),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    BoldButton(
                                        modifier = Modifier.width(150.dp),
                                        enabled = settingsUIState.isEnabled,
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
                        fontSize = FontSize.subtitle,
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
