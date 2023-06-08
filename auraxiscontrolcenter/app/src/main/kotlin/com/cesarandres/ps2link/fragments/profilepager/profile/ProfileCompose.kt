package com.cesarandres.ps2link.fragments.profilepager.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.appfrontend.profilepager.profile.ProfileCompose
import com.cramsan.ps2link.appfrontend.profilepager.profile.ProfileEventHandler
import com.cramsan.ps2link.core.models.Faction
import com.cramsan.ps2link.core.models.LoginStatus
import com.cramsan.ps2link.core.models.Namespace
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Preview
@Composable
fun Preview() {
    ProfileCompose(
        faction = Faction.VS,
        br = 80,
        prestige = 2,
        percentToNextBR = 75f,
        certs = 1000,
        percentToNextCert = 50f,
        loginStatus = LoginStatus.ONLINE,
        lastLogin = "",
        outfit = null,
        server = "Genudine",
        timePlayed = 1000.minutes,
        creationTime = "",
        sessionCount = 100,
        prestigeIcon = null,
        isLoading = true,
        isError = false,
        eventHandler = object : ProfileEventHandler {
            override fun onOutfitSelected(outfitId: String, namespace: Namespace) = Unit
            override fun onRefreshRequested() = Unit
        },
    )
}
