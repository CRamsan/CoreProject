package com.cesarandres.ps2link.fragments.profilepager.friendlist

import androidx.annotation.MainThread
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.core.models.FriendCharacter
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.items.FriendItem

@Composable
fun FriendListCompose(
    friendList: List<FriendCharacter>,
    isLoading: Boolean,
    eventHandler: FriendListEventHandler,
) {
    FrameBottom(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn {
                items(friendList) {
                    FriendItem(
                        modifier = Modifier.fillMaxWidth(),
                        label = it.characterName ?: "",
                        loginStatus = it.loginStatus,
                        onClick = { eventHandler.onProfileSelected(it.characterId, it.namespace) }
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
interface FriendListEventHandler {
    fun onProfileSelected(profileId: String, namespace: Namespace)
}

@Preview
@Composable
fun Preview() {
    FriendListCompose(
        friendList = emptyList(),
        isLoading = true,
        eventHandler = object : FriendListEventHandler {
            override fun onProfileSelected(profileId: String, namespace: Namespace) = Unit
        },
    )
}
