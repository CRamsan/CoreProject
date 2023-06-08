package com.cesarandres.ps2link.fragments.addprofile

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.appfrontend.addprofile.ProfileAddCompose
import com.cramsan.ps2link.appfrontend.addprofile.ProfileAddEventHandler
import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.core.models.CharacterClass
import com.cramsan.ps2link.core.models.Faction
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.core.models.Server
import com.cramsan.ps2link.ui.theme.PS2Theme
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.days

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
