package com.cesarandres.ps2link.fragments.serverlist

import androidx.annotation.MainThread
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.core.models.Population
import com.cramsan.ps2link.core.models.Server
import com.cramsan.ps2link.core.models.ServerStatus
import com.cramsan.ps2link.ui.ErrorOverlay
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.R
import com.cramsan.ps2link.ui.SwipeToRefresh
import com.cramsan.ps2link.ui.items.ServerItem
import com.cramsan.ps2link.ui.theme.PS2Theme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * Render the list [serverItems] of servers and their current status.
 */
@Composable
fun ServerListCompose(
    serverItems: ImmutableList<Server>,
    isLoading: Boolean,
    isError: Boolean,
    eventHandler: ServerListEventHandler,
) {
    FrameBottom {
        Box(modifier = Modifier.fillMaxSize()) {
            SwipeToRefresh(
                isLoading = isLoading,
                onRefreshRequested = { eventHandler.onRefreshRequested() },
            ) {
                items(serverItems) {
                    ServerItem(
                        serverName = it.serverName ?: "",
                        population = it.serverMetadata?.population ?: Population.UNKNOWN,
                        status = it.serverMetadata?.status ?: ServerStatus.UNKNOWN,
                        namespace = it.namespace,
                    )
                }
            }
            ErrorOverlay(isError = isError, error = stringResource(id = R.string.text_unkown_error))
        }
    }
}

@MainThread
interface ServerListEventHandler {
    fun onRefreshRequested()
}

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
