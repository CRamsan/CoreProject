package com.cesarandres.ps2link.fragments.profilepager.statlist

import androidx.annotation.MainThread
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.cesarandres.ps2link.R
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.core.models.StatItem
import com.cramsan.ps2link.ui.ErrorOverlay
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.SwipeToRefresh
import com.cramsan.ps2link.ui.items.StatItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun StatListCompose(
    statList: ImmutableList<StatItem>,
    isLoading: Boolean,
    isError: Boolean,
    eventHandler: StatListEventHandler,
) {
    FrameBottom {
        Box(modifier = Modifier.fillMaxSize()) {
            SwipeToRefresh(
                isLoading = isLoading,
                onRefreshRequested = { eventHandler.onRefreshRequested() },
            ) {
                items(statList) {
                    StatItem(
                        label = it.statName ?: stringResource(R.string.text_unknown),
                        allTime = it.allTime?.toFloat(),
                        today = it.today?.toFloat(),
                        thisWeek = it.thisWeek?.toFloat(),
                        thisMonth = it.thisMonth?.toFloat(),
                    )
                }
            }
            ErrorOverlay(isError = isError)
        }
    }
}

@MainThread
interface StatListEventHandler {
    fun onProfileSelected(profileId: String, namespace: Namespace)
    fun onRefreshRequested()
}

@Preview
@Composable
fun Preview() {
    StatListCompose(
        statList = persistentListOf(),
        isLoading = true,
        isError = false,
        eventHandler = object : StatListEventHandler {
            override fun onProfileSelected(profileId: String, namespace: Namespace) = Unit
            override fun onRefreshRequested() = Unit
        },
    )
}
