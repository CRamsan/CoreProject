package com.cesarandres.ps2link.fragments.profilepager.statlist

import androidx.annotation.MainThread
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.cesarandres.ps2link.R
import com.cramsan.ps2link.appcore.dbg.Namespace
import com.cramsan.ps2link.appcore.dbg.content.character.Stat
import com.cramsan.ps2link.appcore.getThisMonth
import com.cramsan.ps2link.appcore.getThisWeek
import com.cramsan.ps2link.appcore.getToday
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.items.StatItem

@Composable
fun StatListCompose(
    statList: List<Stat>,
    isLoading: Boolean,
    eventHandler: StatListEventHandler,
) {
    FrameBottom(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn {
                items(statList) {
                    StatItem(
                        label = it.stat_name ?: stringResource(R.string.text_unknown),
                        allTime = it.all_time?.toFloat(),
                        today = it.getToday(),
                        thisWeek = it.getThisWeek(),
                        thisMonth = it.getThisMonth(),
                    )
                }
            }
            if (isLoading) {
                CircularProgressIndicator()
            }
        }
    }
}

@MainThread
interface StatListEventHandler {
    fun onProfileSelected(profileId: String, namespace: Namespace)
}

@Preview
@Composable
fun Preview() {
    StatListCompose(
        statList = emptyList(),
        isLoading = true,
        eventHandler = object : StatListEventHandler {
            override fun onProfileSelected(profileId: String, namespace: Namespace) = Unit
        },
    )
}
