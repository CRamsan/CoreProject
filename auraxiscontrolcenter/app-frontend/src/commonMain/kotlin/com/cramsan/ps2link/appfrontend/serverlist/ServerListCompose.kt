package com.cramsan.ps2link.appfrontend.serverlist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cramsan.ps2link.appfrontend.UnknownErrorText
import com.cramsan.ps2link.core.models.Population
import com.cramsan.ps2link.core.models.Server
import com.cramsan.ps2link.core.models.ServerStatus
import com.cramsan.ps2link.ui.ErrorOverlay
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.SwipeToRefresh
import com.cramsan.ps2link.ui.items.ServerItem
import kotlinx.collections.immutable.ImmutableList

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
            ErrorOverlay(isError = isError, error = UnknownErrorText())
        }
    }
}

/**
 *
 */
interface ServerListEventHandler {
    /**
     *
     */
    fun onRefreshRequested()
}
