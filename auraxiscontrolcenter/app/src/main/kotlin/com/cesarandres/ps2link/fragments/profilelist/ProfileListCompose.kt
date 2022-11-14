package com.cesarandres.ps2link.fragments.profilelist

import androidx.annotation.MainThread
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.items.ProfileItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun ProfileListCompose(
    profileItems: ImmutableList<Character>,
    eventHandler: ProfileListEventHandler,
) {
    FrameBottom {
        LazyColumn {
            items(profileItems) {
                ProfileItem(
                    label = it.name ?: "",
                    level = it.battleRank?.toInt() ?: 0,
                    faction = it.faction,
                    namespace = it.namespace,
                    onClick = { eventHandler.onProfileSelected(it.characterId, it.namespace) },
                )
            }
        }
    }
}

@MainThread
interface ProfileListEventHandler {
    fun onProfileSelected(profileId: String, namespace: Namespace)
}

@Preview
@Composable
fun NormalButtonPreview() {
    ProfileListCompose(
        profileItems = persistentListOf(),
        eventHandler = object : ProfileListEventHandler {
            override fun onProfileSelected(profileId: String, namespace: Namespace) = Unit
        },
    )
}
