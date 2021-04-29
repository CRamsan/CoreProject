package com.cesarandres.ps2link.fragments.addoutfit

import androidx.annotation.MainThread
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.cesarandres.ps2link.R
import com.cramsan.ps2link.core.models.Faction
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.core.models.Outfit
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.LoadingOverlay
import com.cramsan.ps2link.ui.SearchField
import com.cramsan.ps2link.ui.items.OutfitItem
import kotlinx.datetime.Instant
import kotlin.time.ExperimentalTime

@Composable
fun OutfitAddCompose(
    tagSearchField: String,
    nameSearchField: String,
    outfitItems: List<Outfit>,
    isLoading: Boolean,
    eventHandler: OutfitAddEventHandler,
) {
    FrameBottom(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row {
                SearchField(
                    modifier = Modifier.weight(1f),
                    value = tagSearchField,
                    hint = stringResource(R.string.text_tag),
                ) { text ->
                    eventHandler.onTagFieldUpdated(text)
                }
                SearchField(
                    modifier = Modifier.weight(3f),
                    value = nameSearchField,
                    hint = stringResource(R.string.text_outfit_name),
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
                            memberCount = it.memberCount,
                            namespace = it.namespace,
                            onClick = { eventHandler.onOutfitSelected(it.id, it.namespace) }
                        )
                    }
                }
                LoadingOverlay(enabled = isLoading)
            }
        }
    }
}

@MainThread
interface OutfitAddEventHandler {
    fun onTagFieldUpdated(searchField: String)
    fun onNameFieldUpdated(searchField: String)
    fun onOutfitSelected(outfitId: String, namespace: Namespace)
}

@OptIn(ExperimentalTime::class)
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
                worldId = "1",
                timeCreated = Instant.fromEpochSeconds(1000000),
                leaderCharacterId = "325354435",
                memberCount = 200,
                namespace = Namespace.PS2PS4US,
            )
        ),
        isLoading = true,
        eventHandler = object : OutfitAddEventHandler {
            override fun onTagFieldUpdated(searchField: String) = Unit
            override fun onNameFieldUpdated(searchField: String) = Unit
            override fun onOutfitSelected(outfitId: String, namespace: Namespace) = Unit
        },
    )
}
