package com.cesarandres.ps2link.fragments.profilepager.friendlist

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.appfrontend.profilepager.friendlist.FriendListCompose
import com.cramsan.ps2link.appfrontend.profilepager.friendlist.FriendListEventHandler
import com.cramsan.ps2link.core.models.Namespace
import kotlinx.collections.immutable.persistentListOf

@Preview
@Composable
fun Preview() {
    FriendListCompose(
        friendList = persistentListOf(),
        isLoading = true,
        isError = false,
        eventHandler = object : FriendListEventHandler {
            override fun onProfileSelected(profileId: String, namespace: Namespace) = Unit
            override fun onRefreshRequested() = Unit
        },
    )
}
