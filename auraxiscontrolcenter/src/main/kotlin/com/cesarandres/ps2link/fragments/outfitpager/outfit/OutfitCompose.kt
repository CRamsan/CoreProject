package com.cesarandres.ps2link.fragments.outfitpager.outfit

import androidx.annotation.MainThread
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.core.models.Faction
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.widgets.FactionIcon

@Composable
fun OutfitCompose(
    faction: Faction?,
    isLoading: Boolean,
    eventHandler: OutfitEventHandler,
) {
    FrameBottom(modifier = Modifier.fillMaxSize()) {
        Box {
            Column {
                // Top Faction icon
                FactionIcon(faction = faction ?: Faction.UNKNOWN)
            }
            if (isLoading) {
                CircularProgressIndicator()
            }
        }
    }
}

@MainThread
interface OutfitEventHandler {
    fun onProfileSelected(profileId: String, namespace: Namespace)
}

@Preview
@Composable
fun Preview() {
    OutfitCompose(
        faction = Faction.VS,
        isLoading = true,
        eventHandler = object : OutfitEventHandler {
            override fun onProfileSelected(profileId: String, namespace: Namespace) = Unit
        },
    )
}
