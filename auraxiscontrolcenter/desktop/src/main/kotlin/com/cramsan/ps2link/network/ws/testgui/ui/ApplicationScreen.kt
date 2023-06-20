package com.cramsan.ps2link.network.ws.testgui.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.network.ws.testgui.application.ApplicationManager
import com.cramsan.ps2link.network.ws.testgui.ui.screens.settings.SettingsContent
import com.cramsan.ps2link.network.ws.testgui.ui.tabs.ApplicationTab
import com.cramsan.ps2link.network.ws.testgui.ui.tabs.OutfitsTab
import com.cramsan.ps2link.network.ws.testgui.ui.tabs.ProfilesTab
import com.cramsan.ps2link.network.ws.testgui.ui.theme.Dimensions
import com.cramsan.ps2link.ui.BoldButton
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.FrameSlim
import com.cramsan.ps2link.ui.theme.PS2Theme
import com.cramsan.ps2link.ui.theme.Padding
import com.cramsan.ps2link.ui.theme.Shapes
import com.cramsan.ps2link.ui.theme.Size
import java.awt.Dimension

/**
 * Render the application GUI components, both the TrayIcon and the Application Window.
 */
@Suppress("LongMethod")
@Composable
fun ApplicationScope.ApplicationGUI(
    applicationManager: ApplicationManager,
    uiModel: ApplicationUIModel,
) {
    val minWindowSize = remember {
        DpSize(
            Dimensions.initialScreenWidth,
            Dimensions.initialScreenHeight,
        )
    }
    val state = rememberWindowState(size = minWindowSize)

    PS2Tray(
        uiModel.trayUIModel,
        applicationManager,
    )

    val windowUIModel = uiModel.windowUIModel
    if (windowUIModel.isVisible) {
        state.isMinimized = false
        Window(
            onCloseRequest = { applicationManager.closeWindow() },
            icon = painterResource(windowUIModel.iconPath),
            resizable = true,
            undecorated = true,
            transparent = true,
            title = "PS2Link",
            state = state,
        ) {
            applicationManager.registerWindow(window)

            WindowDraggableArea {
                window.minimumSize = Dimension(
                    minWindowSize.width.value.toInt(),
                    minWindowSize.height.value.toInt(),
                )
                PS2Theme {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        ApplicationContent(
                            uiModel,
                            applicationManager,
                        )
                    }
                }
            }
        }
    } else {
        applicationManager.deregisterWindow()
    }
}

@Suppress("LongMethod")
@Composable
fun ApplicationContent(
    uiModel: ApplicationUIModel,
    applicationScreenEventHandler: ApplicationScreenEventHandler,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        FrameSlim {
            Row(
                modifier = Modifier
                    .background(color = MaterialTheme.colors.background, shape = Shapes.small),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BoldButton(
                    onClick = { applicationScreenEventHandler.onTabSelected(uiModel.state.profileTab) },
                    modifier = Modifier.height(Size.xlarge)
                ) { Text("Players") }
                BoldButton(
                    onClick = { applicationScreenEventHandler.onTabSelected(uiModel.state.outfitTab) },
                    modifier = Modifier.height(Size.xlarge)
                ) { Text("Outfits") }
                BoldButton(
                    onClick = { applicationScreenEventHandler.onTabSelected(ApplicationTab.Settings) },
                    modifier = Modifier.height(Size.xlarge)
                ) { Text("Settings") }
                Crossfade(
                    uiModel.windowUIModel.title,
                    modifier = Modifier.weight(1f),
                ) { title ->
                    Text(
                        title,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )
                }
                if (uiModel.windowUIModel.showAddButton) {
                    BoldButton(
                        onClick = { applicationScreenEventHandler.onSearchSelected() },
                        modifier = Modifier.height(Size.xlarge).padding(horizontal = Padding.large)
                    ) { Text("+") }
                }
                BoldButton(
                    onClick = { applicationScreenEventHandler.onMinimizeSelected() },
                    modifier = Modifier.height(Size.xlarge)
                ) { Text("-") }
                BoldButton(
                    onClick = { applicationScreenEventHandler.onCloseSelected() },
                    modifier = Modifier.height(Size.xlarge)
                ) { Text("x") }
            }
        }
        Box {
            FrameBottom {
                Crossfade(
                    targetState = uiModel.state.selectedTab,
                ) { targetTab ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        // Make sure to use `targetTab`, not `uiModel.state.selectedTab`.
                        when (targetTab) {
                            is ApplicationTab.Profile -> {
                                ProfilesTab()
                            }
                            is ApplicationTab.Outfit -> {
                                OutfitsTab()
                            }
                            ApplicationTab.Settings -> {
                                SettingsContent()
                            }
                        }
                    }
                }
            }
        }
    }
}

interface ApplicationScreenEventHandler {
    fun onProfilesSelected(characterId: String, namespace: Namespace)

    fun onOutfitSelected(outfitId: String, namespace: Namespace)

    fun onTabSelected(applicationTab: ApplicationTab)

    fun onMinimizeSelected()

    fun onCloseSelected()

    fun onDialogOutsideSelected()

    fun onSearchSelected()
}
