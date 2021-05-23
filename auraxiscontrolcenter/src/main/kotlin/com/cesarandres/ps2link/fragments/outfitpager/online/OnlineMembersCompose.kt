package com.cesarandres.ps2link.fragments.outfitpager.online

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
import com.cramsan.ps2link.ui.items.OnlineMemberItem

@Composable
fun OnlineMembersCompose(
    memberList: List<Character>,
    isLoading: Boolean,
    eventHandler: OnlineMemberEventHandler,
) {
    FrameBottom {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn {
                items(memberList) {
                    OnlineMemberItem(
                        modifier = Modifier.fillMaxWidth(),
                        label = it.name ?: "",
                        characterClass = it.activeProfileId,
                        onClick = { eventHandler.onProfileSelected(it.characterId, it.namespace) }
                    )
                }
            }
            LoadingOverlay(enabled = isLoading)
        }
    }
}

@MainThread
interface OnlineMemberEventHandler {
    fun onProfileSelected(profileId: String, namespace: Namespace)
}

@Preview
@Composable
fun Preview() {
    OnlineMembersCompose(
        memberList = emptyList(),
        isLoading = true,
        eventHandler = object : OnlineMemberEventHandler {
            override fun onProfileSelected(profileId: String, namespace: Namespace) = Unit
        },
    )
}
