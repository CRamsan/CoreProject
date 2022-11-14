package com.cesarandres.ps2link.fragments.outfitlist

import androidx.annotation.MainThread
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.core.models.Outfit
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.items.OutfitItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun OutfitListCompose(
    outfitItems: ImmutableList<Outfit>,
    eventHandler: OutfitListEventHandler,
) {
    FrameBottom {
        LazyColumn {
            items(outfitItems) {
                OutfitItem(
                    name = it.name,
                    tag = it.tag,
                    memberCount = it.memberCount,
                    namespace = it.namespace,
                    onClick = { eventHandler.onOutfitSelected(it.id, it.namespace) },
                )
            }
        }
    }
}

@MainThread
interface OutfitListEventHandler {
    fun onOutfitSelected(outfitId: String, namespace: Namespace)
}

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
