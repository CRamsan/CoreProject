package com.cesarandres.ps2link.fragments.outfitlist

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.appfrontend.outfitlist.OutfitListCompose
import com.cramsan.ps2link.appfrontend.outfitlist.OutfitListEventHandler
import com.cramsan.ps2link.core.models.Namespace
import kotlinx.collections.immutable.persistentListOf

@Preview
@Composable
fun OutfitListComposePreview() {
    OutfitListCompose(
        outfitItems = persistentListOf(),
        eventHandler = object : OutfitListEventHandler {
            override fun onOutfitSelected(outfitId: String, namespace: Namespace) = Unit
        },
    )
}
