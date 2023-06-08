package com.cesarandres.ps2link.fragments.profilepager.statlist

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.appfrontend.profilepager.statlist.StatListCompose
import com.cramsan.ps2link.appfrontend.profilepager.statlist.StatListEventHandler
import com.cramsan.ps2link.core.models.Namespace
import kotlinx.collections.immutable.persistentListOf

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
