package com.cramsan.ps2link.appfrontend.outfitlist

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.cramsan.ps2link.appfrontend.UnknownText
import com.cramsan.ps2link.appfrontend.addoutfit.MemberCountText
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.core.models.Outfit
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.items.OutfitItem
import kotlinx.collections.immutable.ImmutableList

/**
 * Render the list of outfits saved in the app.
 */
@Composable
fun OutfitListCompose(
    outfitItems: ImmutableList<Outfit>,
    eventHandler: OutfitListEventHandler,
) {
    FrameBottom {
        LazyColumn {
            items(outfitItems) {
                OutfitItem(
                    name = it.name ?: UnknownText(),
                    tag = it.tag ?: UnknownText(),
                    memberCount = MemberCountText(it.memberCount),
                    namespace = it.namespace,
                    onClick = { eventHandler.onOutfitSelected(it.id, it.namespace) },
                )
            }
        }
    }
}

interface OutfitListEventHandler {
    fun onOutfitSelected(outfitId: String, namespace: Namespace)
}
