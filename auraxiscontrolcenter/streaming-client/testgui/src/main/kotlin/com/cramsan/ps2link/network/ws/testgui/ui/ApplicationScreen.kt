package com.cramsan.ps2link.network.ws.testgui.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberTrayState
import com.cramsan.ps2link.network.ws.testgui.application.ApplicationManager
import com.cramsan.ps2link.network.ws.testgui.ui.navigation.Navigator

/**
 * Render the application GUI components, both the TrayIcon and the Application Window.
 */
@Composable
fun ApplicationScope.ApplicationGUI(
    navigator: Navigator,
    applicationManager: ApplicationManager,
    uiModel: ApplicationUIModel,
) {
    val trayState = rememberTrayState()

    Tray(
        state = trayState,
        icon = painterResource(uiModel.trayIconPath),
        onAction = { applicationManager.openWindow() },
        menu = {
            Item(
                "Open",
                onClick = { applicationManager.openWindow() },
            )
            val actionLabel = uiModel.actionLabel
            if (actionLabel != null) {
                Separator()
                Item(
                    "Status: ${uiModel.status}",
                    onClick = { applicationManager.openWindow() },
                )
                Item(
                    actionLabel,
                    onClick = { applicationManager.onTrayAction() },
                )
            }

            Separator()
            Item(
                "Exit",
                onClick = { exitApplication() },
            )
        },
    )

    if (uiModel.isWindowOpen) {
        Window(
            onCloseRequest = { applicationManager.closeWindow() },
            icon = painterResource(uiModel.windowIconPath),
            resizable = true,
            undecorated = false,
            title = "PS2Link",
        ) {
            MaterialTheme {
                navigator.renderScreen(uiModel.screenType)
            }
        }
    }
}
