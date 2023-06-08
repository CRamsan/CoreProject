package com.cesarandres.ps2link.fragments.outfitpager.members

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.appfrontend.outfitpager.members.MemberListCompose
import com.cramsan.ps2link.appfrontend.outfitpager.members.MemberListEventHandler
import com.cramsan.ps2link.core.models.Namespace
import kotlinx.collections.immutable.persistentListOf

@Preview
@Composable
fun Preview() {
    MemberListCompose(
        memberList = persistentListOf(),
        isLoading = true,
        isError = false,
        eventHandler = object : MemberListEventHandler {
            override fun onProfileSelected(profileId: String, namespace: Namespace) = Unit
            override fun onRefreshRequested() = Unit
        },
    )
}
