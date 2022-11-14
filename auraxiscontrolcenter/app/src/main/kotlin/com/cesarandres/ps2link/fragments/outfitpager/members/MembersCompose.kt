package com.cesarandres.ps2link.fragments.outfitpager.members

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
import com.cramsan.ps2link.ui.items.OutfitMemberItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun MemberListCompose(
    memberList: ImmutableList<Character>,
    isLoading: Boolean,
    isError: Boolean,
    eventHandler: MemberListEventHandler,
) {
    FrameBottom {
        Box(modifier = Modifier.fillMaxSize()) {
            SwipeToRefresh(
                isLoading = isLoading,
                onRefreshRequested = { eventHandler.onRefreshRequested() },
            ) {
                items(memberList) {
                    OutfitMemberItem(
                        modifier = Modifier.fillMaxWidth(),
                        label = it.name ?: "",
                        loginStatus = it.loginStatus,
                        outfitRank = it.outfitRank?.name ?: "",
                        onClick = { eventHandler.onProfileSelected(it.characterId, it.namespace) },
                    )
                }
            }
            ErrorOverlay(isError = isError)
        }
    }
}

@MainThread
interface MemberListEventHandler {
    fun onProfileSelected(profileId: String, namespace: Namespace)
    fun onRefreshRequested()
}

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
