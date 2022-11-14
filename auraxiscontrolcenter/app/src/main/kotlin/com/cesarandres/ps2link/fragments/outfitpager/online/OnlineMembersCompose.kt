package com.cesarandres.ps2link.fragments.outfitpager.online

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
import com.cramsan.ps2link.ui.items.OnlineMemberItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * Render [memberList] as a column of members with their online status.
 */
@Composable
fun OnlineMembersCompose(
    memberList: ImmutableList<Character>,
    isLoading: Boolean,
    isError: Boolean,
    eventHandler: OnlineMemberEventHandler,
) {
    FrameBottom {
        Box(modifier = Modifier.fillMaxSize()) {
            SwipeToRefresh(
                isLoading = isLoading,
                onRefreshRequested = { eventHandler.onRefreshRequested() },
            ) {
                items(memberList) {
                    OnlineMemberItem(
                        modifier = Modifier.fillMaxWidth(),
                        label = it.name ?: "",
                        characterClass = it.activeProfileId,
                        onClick = { eventHandler.onProfileSelected(it.characterId, it.namespace) },
                    )
                }
            }
            ErrorOverlay(isError = isError)
        }
    }
}

@MainThread
interface OnlineMemberEventHandler {
    fun onProfileSelected(profileId: String, namespace: Namespace)
    fun onRefreshRequested()
}

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
