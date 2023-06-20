package com.cesarandres.ps2link.fragments.profilepager.killlist

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.appfrontend.profilepager.killlist.KillListCompose
import com.cramsan.ps2link.appfrontend.profilepager.killlist.KillListEventHandler
import com.cramsan.ps2link.core.models.Namespace
import kotlinx.collections.immutable.persistentListOf

@Preview
@Composable
fun Preview() {
    KillListCompose(
        killList = persistentListOf(),
        useVerticalMode = false,
        isLoading = true,
        isError = false,
        eventHandler = object : KillListEventHandler {
            override fun onProfileSelected(profileId: String, namespace: Namespace) = Unit
            override fun onRefreshRequested() = Unit
        },
    )
}
