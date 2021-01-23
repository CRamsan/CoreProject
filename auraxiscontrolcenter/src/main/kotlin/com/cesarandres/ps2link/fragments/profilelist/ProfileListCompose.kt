package com.cesarandres.ps2link.fragments.profilelist

import androidx.annotation.MainThread
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.appcore.dbg.Namespace
import com.cramsan.ps2link.appcore.dbg.content.CharacterProfile
import com.cramsan.ps2link.ui.FrameBottom

@Composable
fun ProfileListCompose(
    profileItems: List<CharacterProfile>,
    eventHandler: ProfileListEventHandler,
) {
    FrameBottom(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            items(profileItems) {
            }
        }
    }
}

@MainThread
interface ProfileListEventHandler {
    fun onSearchProfileClick()
    fun onProfileSelected(profileId: String, namespace: Namespace)
}

@Preview
@Composable
fun NormalButtonPreview() {
    ProfileListCompose(
        profileItems = emptyList(),
        eventHandler = object : ProfileListEventHandler {
            override fun onSearchProfileClick() = Unit
            override fun onProfileSelected(profileId: String, namespace: Namespace) = Unit
        }
    )
}
