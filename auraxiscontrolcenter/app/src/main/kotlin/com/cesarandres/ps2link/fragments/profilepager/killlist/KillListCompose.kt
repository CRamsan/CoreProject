package com.cesarandres.ps2link.fragments.profilepager.killlist

import androidx.annotation.MainThread
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.core.models.KillEvent
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.ui.ErrorOverlay
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.SwipeToRefresh
import com.cramsan.ps2link.ui.items.KillItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun KillListCompose(
    killList: ImmutableList<KillEvent>,
    isLoading: Boolean,
    isError: Boolean,
    eventHandler: KillListEventHandler,
) {
    FrameBottom {
        Box(modifier = Modifier.fillMaxSize()) {
            SwipeToRefresh(
                isLoading = isLoading,
                onRefreshRequested = { eventHandler.onRefreshRequested() },
            ) {
                items(killList) {
                    KillItem(
                        modifier = Modifier.fillMaxWidth(),
                        killType = it.killType,
                        faction = it.faction,
                        attacker = it.attacker,
                        time = it.time,
                        weaponName = it.weaponName,
                        weaponImage = it.weaponImage,
                        onClick = {
                            it.characterId?.let { characterId ->
                                eventHandler.onProfileSelected(characterId, it.namespace)
                            }
                        },
                    )
                }
            }
            ErrorOverlay(isError = isError)
        }
    }
}

@MainThread
interface KillListEventHandler {
    fun onProfileSelected(profileId: String, namespace: Namespace)
    fun onRefreshRequested()
}

@Preview
@Composable
fun Preview() {
    KillListCompose(
        killList = persistentListOf(),
        isLoading = true,
        isError = false,
        eventHandler = object : KillListEventHandler {
            override fun onProfileSelected(profileId: String, namespace: Namespace) = Unit
            override fun onRefreshRequested() = Unit
        },
    )
}
