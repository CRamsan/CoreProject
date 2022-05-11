package com.cesarandres.ps2link.fragments.profilepager.friendlist

import androidx.annotation.MainThread
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.ui.ErrorOverlay
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.SwipeToRefresh
import com.cramsan.ps2link.ui.items.FriendItem

@Composable
fun FriendListCompose(
    friendList: List<Character>,
    isLoading: Boolean,
    isError: Boolean,
    eventHandler: FriendListEventHandler,
) {
    FrameBottom {
        Box(modifier = Modifier.fillMaxSize()) {
            SwipeToRefresh(
                isLoading = isLoading,
                onRefreshRequested = { eventHandler.onRefreshRequested() },
            ) {
                items(friendList) {
                    FriendItem(
                        modifier = Modifier.fillMaxWidth(),
                        label = it.name ?: "",
                        loginStatus = it.loginStatus,
                        onClick = { eventHandler.onProfileSelected(it.characterId, it.namespace) },
                    )
                }
            }
            ErrorOverlay(isError = isError)
        }
    }
}

@MainThread
interface FriendListEventHandler {
    fun onProfileSelected(profileId: String, namespace: Namespace)
    fun onRefreshRequested()
}

@Preview
@Composable
fun Preview() {
    FriendListCompose(
        friendList = emptyList(),
        isLoading = true,
        isError = false,
        eventHandler = object : FriendListEventHandler {
            override fun onProfileSelected(profileId: String, namespace: Namespace) = Unit
            override fun onRefreshRequested() = Unit
        },
    )
}
