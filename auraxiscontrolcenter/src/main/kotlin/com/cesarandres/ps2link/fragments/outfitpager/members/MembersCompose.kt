package com.cesarandres.ps2link.fragments.outfitpager.members

import androidx.annotation.MainThread
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.LoadingOverlay
import com.cramsan.ps2link.ui.items.OutfitMemberItem

@Composable
fun MemberListCompose(
    memberList: List<Character>,
    isLoading: Boolean,
    eventHandler: MemberListEventHandler,
) {
    FrameBottom {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn {
                items(memberList) {
                    OutfitMemberItem(
                        modifier = Modifier.fillMaxWidth(),
                        label = it.name ?: "",
                        loginStatus = it.loginStatus,
                        outfitRank = it.outfitRank?.name ?: "",
                        onClick = { eventHandler.onProfileSelected(it.characterId, it.namespace) }
                    )
                }
            }
            LoadingOverlay(enabled = isLoading)
        }
    }
}

@MainThread
interface MemberListEventHandler {
    fun onProfileSelected(profileId: String, namespace: Namespace)
}

@Preview
@Composable
fun Preview() {
    MemberListCompose(
        memberList = emptyList(),
        isLoading = true,
        eventHandler = object : MemberListEventHandler {
            override fun onProfileSelected(profileId: String, namespace: Namespace) = Unit
        },
    )
}
