package com.cesarandres.ps2link.fragments.profilepager.killlist

import androidx.annotation.MainThread
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.core.models.KillEvent
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.LoadingOverlay
import com.cramsan.ps2link.ui.items.KillItem

@Composable
fun KillListCompose(
    killList: List<KillEvent>,
    isLoading: Boolean,
    eventHandler: KillListEventHandler,
) {
    FrameBottom(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn {
                items(killList) {
                    KillItem(
                        modifier = Modifier.fillMaxWidth(),
                        killType = it.killType,
                        faction = it.faction,
                        attacker = it.attacker,
                        time = it.time,
                        weaponName = it.weaponName,
                        onClick = {
                            it.characterId?.let { characterId ->
                                eventHandler.onProfileSelected(characterId, it.namespace)
                            }
                        }
                    )
                }
            }
            LoadingOverlay(enabled = isLoading)
        }
    }
}

@MainThread
interface KillListEventHandler {
    fun onProfileSelected(profileId: String, namespace: Namespace)
}

@Preview
@Composable
fun Preview() {
    KillListCompose(
        killList = emptyList(),
        isLoading = true,
        eventHandler = object : KillListEventHandler {
            override fun onProfileSelected(profileId: String, namespace: Namespace) = Unit
        },
    )
}
