package com.cesarandres.ps2link.fragments.addoutfit

import androidx.annotation.MainThread
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.core.models.Outfit
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.items.OutfitItem
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
            TextField(
                value = tagSearchField,
                onValueChange = { eventHandler.onTagFieldUpdated(it) }
            )

            TextField(
                value = nameSearchField,
                onValueChange = { eventHandler.onNameFieldUpdated(it) }
            )

            Box {
                LazyColumn {
                    items(outfitItems) {
                        OutfitItem(
                            label = it.name ?: "",
                            onClick = { eventHandler.onOutfitSelected(it.id, it.namespace) }
                        )
                    }
                }
                if (isLoading) {
                    CircularProgressIndicator()
                }
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
