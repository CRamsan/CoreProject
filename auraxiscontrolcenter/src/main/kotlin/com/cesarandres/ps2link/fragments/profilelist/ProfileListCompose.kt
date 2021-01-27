package com.cesarandres.ps2link.fragments.profilelist

import androidx.annotation.MainThread
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cesarandres.ps2link.toUIFaction
import com.cramsan.ps2link.appcore.dbg.Namespace
import com.cramsan.ps2link.db.Character
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.items.ProfileItem

@Composable
fun ProfileListCompose(
    profileItems: List<Character>,
    eventHandler: ProfileListEventHandler,
) {
    FrameBottom(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            items(profileItems) {
                ProfileItem(
                    label = it.name ?: "",
                    level = it.rank?.toInt(),
                    faction = it.factionId.toUIFaction(),
                    onClick = { eventHandler.onProfileSelected(it.id, it.namespace) }
                )
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
