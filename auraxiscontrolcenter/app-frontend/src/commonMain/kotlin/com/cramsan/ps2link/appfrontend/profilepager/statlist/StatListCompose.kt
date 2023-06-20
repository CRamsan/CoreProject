package com.cramsan.ps2link.appfrontend.profilepager.statlist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cramsan.ps2link.appfrontend.UnknownErrorText
import com.cramsan.ps2link.appfrontend.UnknownText
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.core.models.StatItem
import com.cramsan.ps2link.ui.ErrorOverlay
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.SwipeToRefresh
import com.cramsan.ps2link.ui.items.StatItemHorizontal
import com.cramsan.ps2link.ui.items.StatItemVertical
import kotlinx.collections.immutable.ImmutableList

/**
 * Render a lit of stats for a given profile.
 */
@Suppress("MaximumLineLength")
@Composable
fun StatListCompose(
    statList: ImmutableList<StatItem>,
    isLoading: Boolean,
    isError: Boolean,
    useVerticalMode: Boolean = false,
    eventHandler: StatListEventHandler,
) {
    FrameBottom {
        Box(modifier = Modifier.fillMaxSize()) {
            SwipeToRefresh(
                isLoading = isLoading,
                onRefreshRequested = { eventHandler.onRefreshRequested() },
            ) {
                items(statList) {
                    if (useVerticalMode) {
                        StatItemVertical(
                            label = it.statName ?: UnknownText(),
                            allTime = it.allTime,
                            today = it.today,
                            thisWeek = it.thisWeek,
                            thisMonth = it.thisMonth,
                        )
                    } else {
                        StatItemHorizontal(
                            label = it.statName ?: UnknownText(),
                            allTime = it.allTime,
                            today = it.today,
                            thisWeek = it.thisWeek,
                            thisMonth = it.thisMonth,
                        )
                    }
                }
            }
            ErrorOverlay(
                isError = isError,
                error = UnknownErrorText()
            )
        }
    }
}

/**
 *
 */
interface StatListEventHandler {
    /**
     *
     */
    fun onProfileSelected(profileId: String, namespace: Namespace)
    /**
     *
     */
    fun onRefreshRequested()
}
