package com.cesarandres.ps2link.fragments.serverlist

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.core.models.Server
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.items.ServerItem

@Composable
fun ServerListCompose(
    serverItems: List<Server>,
) {
    FrameBottom(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            items(serverItems) {
                ServerItem(
                    serverName = it.serverName
                )
            }
        }
    }
}

@Preview
@Composable
fun ServerListPreview() {
    ServerListCompose(
        serverItems = emptyList(),
    )
}
