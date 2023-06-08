package com.cesarandres.ps2link.fragments.outfitpager.outfit

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.appfrontend.outfitpager.outfit.OutfitCompose
import com.cramsan.ps2link.appfrontend.outfitpager.outfit.OutfitEventHandler
import com.cramsan.ps2link.core.models.Faction
import com.cramsan.ps2link.core.models.Namespace

@Preview
@Composable
fun Preview() {
    OutfitCompose(
        faction = Faction.VS,
        isLoading = true,
        eventHandler = object : OutfitEventHandler {
            override fun onProfileSelected(profileId: String, namespace: Namespace) = Unit
            override fun onRefreshRequested() = Unit
        },
    )
}
