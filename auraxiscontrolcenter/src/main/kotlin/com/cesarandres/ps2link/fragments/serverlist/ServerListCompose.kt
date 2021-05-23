package com.cesarandres.ps2link.fragments.serverlist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.core.models.Population
import com.cramsan.ps2link.core.models.Server
import com.cramsan.ps2link.core.models.ServerStatus
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.LoadingOverlay
import com.cramsan.ps2link.ui.items.ServerItem
import com.cramsan.ps2link.ui.theme.PS2Theme

@Composable
fun ServerListCompose(
    serverItems: List<Server>,
    isLoading: Boolean,
) {
    FrameBottom {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn {
                items(serverItems) {
                    ServerItem(
                        serverName = it.serverName ?: "",
                        population = it.serverMetadata?.population ?: Population.UNKNOWN,
                        status = it.serverMetadata?.status ?: ServerStatus.UNKNOWN,
                        namespace = it.namespace
                    )
                }
            }
            LoadingOverlay(enabled = isLoading)
        }
    }
}

@Preview
@Composable
fun ServerListPreview() {
    PS2Theme {
        ServerListCompose(
            serverItems = emptyList(),
            isLoading = false,
        )
    }
}
