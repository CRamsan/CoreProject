package com.cramsan.ps2link.network.ws.testgui.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberTrayState
import androidx.compose.ui.window.rememberWindowState
import com.cramsan.ps2link.network.ws.testgui.application.ApplicationManager
import com.cramsan.ps2link.network.ws.testgui.ui.navigation.Navigator
import com.cramsan.ps2link.network.ws.testgui.ui.theme.ScreenSizes
import com.cramsan.ps2link.ui.BoldButton
import com.cramsan.ps2link.ui.theme.PS2Theme
import com.cramsan.ps2link.ui.theme.Shapes
import com.cramsan.ps2link.ui.theme.Size
import com.cramsan.ps2link.ui.theme.primaryColor
import java.awt.Dimension

/**
 * Render the application GUI components, both the TrayIcon and the Application Window.
 */
@Suppress("LongMethod")
@Composable
fun ApplicationScope.ApplicationGUI(
    navigator: Navigator,
    applicationManager: ApplicationManager,
    uiModel: ApplicationUIModel,
) {
    val trayState = rememberTrayState()
    val minWindowSize = remember {
        DpSize(
            ScreenSizes.initialScreenWidth,
            ScreenSizes.initialScreenHeight,
        )
    }
    var isFirstOpen by remember { mutableStateOf(true) }
    val state = rememberWindowState(size = minWindowSize)

    val trayUIModel = uiModel.trayUIModel
    Tray(
        state = trayState,
        icon = painterResource(trayUIModel.iconPath),
        onAction = {
            state.isMinimized = false
            applicationManager.openWindow()
        },
        menu = {
            Item(
                "Open",
                onClick = {
                    applicationManager.openWindow()
                    state.isMinimized = false
                },
            )
            val actionLabel = trayUIModel.actionLabel
            if (actionLabel != null) {
                Separator()
                Item(
                    "Status: ${trayUIModel.statusLabel}",
                    onClick = {
                        applicationManager.openWindow()
                        state.isMinimized = false
                    },
                )
                Item(
                    actionLabel,
                    onClick = { applicationManager.onTrayAction() },
                )
            }

            Separator()
            Item(
                "Exit",
                onClick = { applicationManager.exitApplication() },
            )
        },
    )

    val windowUIModel = uiModel.windowUIModel
    if (windowUIModel.isVisible) {
        Window(
            onCloseRequest = { applicationManager.closeWindow() },
            icon = painterResource(windowUIModel.iconPath),
            resizable = true,
            undecorated = true,
            transparent = true,
            title = "PS2Link",
            state = state,
        ) {
            WindowDraggableArea {
                window.minimumSize = Dimension(
                    minWindowSize.width.value.toInt(),
                    minWindowSize.height.value.toInt(),
                )
                PS2Theme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = Color.Black,
                        shape = Shapes.large,
                        border = BorderStroke(Size.micro, primaryColor),
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            navigator.renderScreen(uiModel.state.screenType)
                            Row(
                                modifier = Modifier.align(Alignment.TopEnd)
                                    .background(color = Color.Black, shape = Shapes.small),
                            ) {
                                BoldButton(
                                    onClick = { state.isMinimized = true },
                                    modifier = Modifier.height(Size.xlarge)
                                ) { Text("-") }
                                BoldButton(
                                    onClick = { applicationManager.closeWindow() },
                                    modifier = Modifier.height(Size.xlarge)
                                ) { Text("x") }
                            }
                        }
                    }
                }
                if (isFirstOpen) {
                    applicationManager.registerWindow(window)
                    state.isMinimized = false
                }
                isFirstOpen = false
            }
        }
    } else {
        applicationManager.deregisterWindow()
        isFirstOpen = true
    }
}
