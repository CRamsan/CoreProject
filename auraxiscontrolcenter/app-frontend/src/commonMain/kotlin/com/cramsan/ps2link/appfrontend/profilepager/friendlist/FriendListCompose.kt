package com.cramsan.ps2link.appfrontend.profilepager.friendlist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cramsan.ps2link.appfrontend.UnknownErrorText
import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.ui.ErrorOverlay
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.SwipeToRefresh
import com.cramsan.ps2link.ui.items.FriendItem
import kotlinx.collections.immutable.ImmutableList

/**
 * Render a list of friends.
 */
@Composable
fun FriendListCompose(
    friendList: ImmutableList<Character>,
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
            ErrorOverlay(isError = isError, error = UnknownErrorText())
        }
    }
}

/**
 *
 */
interface FriendListEventHandler {
    /**
     *
     */
    fun onProfileSelected(profileId: String, namespace: Namespace)
    /**
     *
     */
    fun onRefreshRequested()
}