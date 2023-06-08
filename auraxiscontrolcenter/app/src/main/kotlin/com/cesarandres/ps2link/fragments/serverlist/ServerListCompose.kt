package com.cesarandres.ps2link.fragments.serverlist

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.appfrontend.serverlist.ServerListCompose
import com.cramsan.ps2link.appfrontend.serverlist.ServerListEventHandler
import com.cramsan.ps2link.ui.theme.PS2Theme
import kotlinx.collections.immutable.persistentListOf

@Preview
@Composable
fun ServerListPreview() {
    PS2Theme {
        ServerListCompose(
            serverItems = persistentListOf(),
            isLoading = false,
            isError = true,
            eventHandler = object : ServerListEventHandler {
                override fun onRefreshRequested() = Unit
            },
        )
    }
}
