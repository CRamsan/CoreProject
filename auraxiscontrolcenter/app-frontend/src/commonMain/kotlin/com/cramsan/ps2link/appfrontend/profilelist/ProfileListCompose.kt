package com.cramsan.ps2link.appfrontend.profilelist

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.items.ProfileItem
import kotlinx.collections.immutable.ImmutableList

/**
 * Render the list of saved profiles.
 */
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

/**
 *
 */
interface ProfileListEventHandler {
    /**
     *
     */
    fun onProfileSelected(profileId: String, namespace: Namespace)
}
