package com.cesarandres.ps2link.fragments.addprofile

import androidx.annotation.MainThread
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cesarandres.ps2link.toUIFaction
import com.cramsan.ps2link.appcore.dbg.Faction
import com.cramsan.ps2link.appcore.dbg.Namespace
import com.cramsan.ps2link.db.Character
import com.cramsan.ps2link.ui.items.ProfileItem

@Composable
fun ProfileAddCompose(
    searchField: String,
    profileItems: List<Character>,
    isLoading: Boolean,
    eventHandler: ProfileAddEventHandler,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TextField(
            value = searchField,
            onValueChange = { eventHandler.onSearchFieldUpdated(it) }
        )

        Box {
            LazyColumn {
                items(profileItems) {
                    ProfileItem(
                        label = it.name ?: "",
                        level = it.rank?.toInt(),
                        faction = it.factionId?.toUIFaction()
                    )
                }
            }
            if (isLoading) {
                CircularProgressIndicator()
            }
        }
    }
}

@MainThread
interface ProfileAddEventHandler {
    fun onSearchFieldUpdated(searchField: String)
    fun onProfileSelected(profileId: String)
}

@Preview
@Composable
fun NormalButtonPreview() {
    ProfileAddCompose(
        searchField = "Cramsan",
        profileItems = listOf(
            Character(
                id = "",
                name = "Cramsan1",
                rank = 80,
                factionId = Faction.VS,
                namespace = Namespace.PS2PS4US.name,
                activeProfileId = 1,
                currentPoints = 1,
                lastLogin = 0,
                lastUpdated = 0,
                minutesPlayed = 0,
                outfitName = null,
                percentageToNextCert = 0.0,
                percentageToNextRank = 0.0,
                worldId = "",
                worldName = "",
            )
        ),
        isLoading = true,
        eventHandler = object : ProfileAddEventHandler {
            override fun onSearchFieldUpdated(searchField: String) = Unit
            override fun onProfileSelected(profileId: String) = Unit
        },
    )
}
