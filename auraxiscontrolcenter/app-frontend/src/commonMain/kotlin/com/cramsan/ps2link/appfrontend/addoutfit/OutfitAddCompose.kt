package com.cramsan.ps2link.appfrontend.addoutfit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cramsan.ps2link.appfrontend.UnknownErrorText
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.core.models.Outfit
import com.cramsan.ps2link.ui.ErrorOverlay
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.LoadingOverlay
import com.cramsan.ps2link.ui.SearchField
import com.cramsan.ps2link.ui.items.OutfitItem
import kotlinx.collections.immutable.ImmutableList

/**
 * Render the page to add outfits to the app.
 */
@Composable
fun OutfitAddCompose(
    tagSearchField: String,
    nameSearchField: String,
    outfitItems: ImmutableList<Outfit>,
    isLoading: Boolean,
    isError: Boolean,
    eventHandler: OutfitAddEventHandler,
) {
    FrameBottom {
        Column(modifier = Modifier.fillMaxSize()) {
            Row {
                SearchField(
                    modifier = Modifier.weight(1f),
                    value = tagSearchField,
                    hint = TagText(),
                ) { text ->
                    eventHandler.onTagFieldUpdated(text)
                }
                SearchField(
                    modifier = Modifier.weight(3f),
                    value = nameSearchField,
                    hint = OutfitNameText(),
                ) { text ->
                    eventHandler.onNameFieldUpdated(text)
                }
            }
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn {
                    items(outfitItems) {
                        OutfitItem(
                            tag = it.tag ?: "",
                            name = it.name ?: "",
                            memberCount = MemberCountText(it.memberCount),
                            namespace = it.namespace,
                            onClick = { eventHandler.onOutfitSelected(it.id, it.namespace) },
                        )
                    }
                }
                LoadingOverlay(enabled = isLoading)
                ErrorOverlay(
                    isError = isError,
                    error = UnknownErrorText()
                )
            }
        }
    }
}

/**
 *
 */
interface OutfitAddEventHandler {
    /**
     *
     */
    fun onTagFieldUpdated(searchField: String)
    /**
     *
     */
    fun onNameFieldUpdated(searchField: String)
    /**
     *
     */
    fun onOutfitSelected(outfitId: String, namespace: Namespace)
}

@Composable
expect fun TagText(): String

@Composable
expect fun OutfitNameText(): String

@Composable
expect fun MemberCountText(memberCount: Int): String
