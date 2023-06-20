package com.cramsan.ps2link.network.ws.testgui.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.rememberTrayState

@Composable
fun ApplicationScope.PS2Tray(
    trayUIModel: ApplicationUIModel.TrayUIModel,
    eventHandler: PS2TrayEventHandler,
) {
    val trayState = rememberTrayState()

    Tray(
        state = trayState,
        icon = painterResource(trayUIModel.iconPath),
        onAction = { eventHandler.onOpenApplicationSelected() },
        menu = {
            Item(
                "Open",
                onClick = { eventHandler.onOpenApplicationSelected() },
            )
            val actionLabel = trayUIModel.actionLabel
            if (actionLabel != null) {
                Separator()
                Item(
                    "Status: ${trayUIModel.statusLabel}",
                    onClick = { eventHandler.onOpenApplicationSelected() },
                )
                Item(
                    actionLabel,
                    onClick = { eventHandler.onPrimaryActionSelected() },
                )
            }

            Separator()
            Item(
                "Exit",
                onClick = { eventHandler.onCloseApplicationSelected() },
            )
        },
    )
}

interface PS2TrayEventHandler {
    fun onOpenApplicationSelected()
    fun onPrimaryActionSelected()
    fun onCloseApplicationSelected()
}
