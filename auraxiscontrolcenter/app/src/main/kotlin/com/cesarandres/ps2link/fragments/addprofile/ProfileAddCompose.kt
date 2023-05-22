package com.cesarandres.ps2link.fragments.addprofile

import androidx.annotation.MainThread
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.cesarandres.ps2link.R
import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.core.models.CharacterClass
import com.cramsan.ps2link.core.models.Faction
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.core.models.Server
import com.cramsan.ps2link.ui.ErrorOverlay
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.FrameSlim
import com.cramsan.ps2link.ui.LoadingOverlay
import com.cramsan.ps2link.ui.SearchField
import com.cramsan.ps2link.ui.items.ProfileItem
import com.cramsan.ps2link.ui.theme.PS2Theme
import com.cramsan.ps2link.ui.theme.Padding
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.days
import kotlin.time.ExperimentalTime

/**
 * Render a page to add a profile to track in the app.
 */
@Composable
fun ProfileAddCompose(
    searchField: String,
    profileItems: ImmutableList<Character>,
    isLoading: Boolean,
    isError: Boolean,
    eventHandler: ProfileAddEventHandler,
) {
    FrameBottom {
        Column(modifier = Modifier.fillMaxSize()) {
            FrameSlim(modifier = Modifier.fillMaxWidth()) {
                SearchField(
                    value = searchField,
                    hint = stringResource(com.cramsan.ps2link.ui.R.string.text_player_name),
                ) { text ->
                    eventHandler.onSearchFieldUpdated(text)
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn {
                    items(profileItems) {
                        ProfileItem(
                            modifier = Modifier.fillMaxWidth().padding(vertical = Padding.micro),
                            label = it.name ?: "",
                            level = it.battleRank?.toInt() ?: 0,
                            faction = it.faction,
                            namespace = it.namespace,
                            onClick = { eventHandler.onProfileSelected(it.characterId, it.namespace) },
                        )
                    }
                }

                LoadingOverlay(enabled = isLoading)
                ErrorOverlay(isError = isError)
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
                    prestige = 2,
                    faction = Faction.VS,
                    namespace = Namespace.PS2PS4US,
                    activeProfileId = CharacterClass.UNKNOWN,
                    certs = 1,
                    lastLogin = Instant.DISTANT_PAST,
                    creationTime = null,
                    sessionCount = null,
                    timePlayed = 10.days,
                    outfit = null,
                    percentageToNextCert = 0.0,
                    percentageToNextBattleRank = 0.0,
                    server = Server(
                        "",
                        Namespace.PS2PC,
                        "Ceres",
                        null,
                    ),
                    cached = true,
                ),
            ).toImmutableList(),
            isLoading = true,
            isError = false,
            eventHandler = object : ProfileAddEventHandler {
                override fun onSearchFieldUpdated(searchField: String) = Unit
                override fun onProfileSelected(profileId: String, namespace: Namespace) = Unit
            },
        )
    }
}
