package com.cesarandres.ps2link.fragments.addoutfit

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.appfrontend.addoutfit.OutfitAddCompose
import com.cramsan.ps2link.appfrontend.addoutfit.OutfitAddEventHandler
import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.core.models.Faction
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.core.models.Outfit
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.Instant

@Preview
@Composable
fun OutfitAddComposePreview() {
    OutfitAddCompose(
        tagSearchField = "D3RP",
        nameSearchField = "Derp Company",
        outfitItems = listOf(
            Outfit(
                id = "",
                name = "Cramsan1",
                tag = "D3rp",
                faction = Faction.VS,
                timeCreated = Instant.fromEpochSeconds(1000000),
                leader = Character(
                    "325354435",
                    "Test",
                    prestige = null,
                    creationTime = null,
                    sessionCount = null,
                    cached = false,
                    namespace = Namespace.PS2PC,
                ),
                memberCount = 200,
                cached = false,
                namespace = Namespace.PS2PS4US,
            ),
        ).toImmutableList(),
        isLoading = true,
        isError = false,
        eventHandler = object : OutfitAddEventHandler {
            override fun onTagFieldUpdated(searchField: String) = Unit
            override fun onNameFieldUpdated(searchField: String) = Unit
            override fun onOutfitSelected(outfitId: String, namespace: Namespace) = Unit
        },
    )
}
