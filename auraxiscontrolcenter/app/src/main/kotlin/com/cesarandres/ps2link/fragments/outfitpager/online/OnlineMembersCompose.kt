package com.cesarandres.ps2link.fragments.outfitpager.online

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.appfrontend.outfitpager.online.OnlineMemberEventHandler
import com.cramsan.ps2link.appfrontend.outfitpager.online.OnlineMembersCompose
import com.cramsan.ps2link.core.models.Namespace
import kotlinx.collections.immutable.persistentListOf

@Preview
@Composable
fun Preview() {
    OnlineMembersCompose(
        memberList = persistentListOf(),
        isLoading = true,
        isError = false,
        eventHandler = object : OnlineMemberEventHandler {
            override fun onProfileSelected(profileId: String, namespace: Namespace) = Unit
            override fun onRefreshRequested() = Unit
        },
    )
}
