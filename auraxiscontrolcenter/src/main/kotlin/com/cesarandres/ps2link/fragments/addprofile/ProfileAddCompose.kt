package com.cesarandres.ps2link.fragments.addprofile

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
import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.core.models.Faction
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.core.models.Server
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.FrameSlim
import com.cramsan.ps2link.ui.items.ProfileItem
import com.cramsan.ps2link.ui.theme.PS2Theme
import kotlinx.datetime.Instant
import kotlin.time.ExperimentalTime
import kotlin.time.days

@Composable
fun ProfileAddCompose(
    searchField: String,
    profileItems: List<Character>,
    isLoading: Boolean,
    eventHandler: ProfileAddEventHandler,
) {
    FrameBottom(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            FrameSlim {
                TextField(
                    value = searchField,
                    // modifier = Modifier.focus(),
                    /*
                    label = {
                        val text = stringResource(R.string.text_player_name)
                        Text(text)
                    },
                     */
                    maxLines = 1,
                    /*
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.None,
                        autoCorrect = false,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                    ),
                    onImeActionPerformed = { _, controller ->
                        controller.hideSoftwareKeyboard()
                    },
                     */
                    onValueChange = { text ->
                        eventHandler.onSearchFieldUpdated(text)
                    }
                )
            }

            Box {
                LazyColumn {
                    items(profileItems) {
                        ProfileItem(
                            label = it.name ?: "",
                            level = it.battleRank?.toInt(),
                            faction = it.faction,
                            onClick = { eventHandler.onProfileSelected(it.characterId, it.namespace) }
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
interface ProfileAddEventHandler {
    fun onSearchFieldUpdated(searchField: String)
    fun onProfileSelected(profileId: String, namespace: Namespace)
}

@OptIn(ExperimentalTime::class)
@Preview
@Composable
fun NormalButtonPreview() {
    PS2Theme {
        ProfileAddCompose(
            searchField = "Cramsan",
            profileItems = listOf(
                Character(
                    characterId = "",
                    name = "Cramsan1",
                    battleRank = 80,
                    faction = Faction.VS,
                    namespace = Namespace.PS2PS4US,
                    activeProfileId = 1,
                    certs = 1,
                    lastLogin = Instant.DISTANT_PAST,
                    timePlayed = 10.days,
                    outfit = null,
                    percentageToNextCert = 0.0,
                    percentageToNextBattleRank = 0.0,
                    server = Server("", "Ceres"),
                    cached = true,
                )
            ),
            isLoading = true,
            eventHandler = object : ProfileAddEventHandler {
                override fun onSearchFieldUpdated(searchField: String) = Unit
                override fun onProfileSelected(profileId: String, namespace: Namespace) = Unit
            },
        )
    }
}
