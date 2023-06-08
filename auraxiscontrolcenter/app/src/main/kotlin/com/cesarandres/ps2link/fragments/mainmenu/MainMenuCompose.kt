package com.cesarandres.ps2link.fragments.mainmenu

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.appfrontend.mainmenu.MainMenuCompose
import com.cramsan.ps2link.appfrontend.mainmenu.MainMenuEventHandler
import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.core.models.CharacterClass
import com.cramsan.ps2link.core.models.Faction
import com.cramsan.ps2link.core.models.LoginStatus
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.core.models.Outfit
import com.cramsan.ps2link.core.models.Server
import com.cramsan.ps2link.ui.theme.PS2Theme
import kotlinx.datetime.Instant

@Preview(
    showBackground = true,
    backgroundColor = 0xFF000000,
)
@Composable
fun NormalButtonPreview() {
    PS2Theme {
        MainMenuCompose(
            preferredProfile = Character(
                "",
                "CRamsan",
                CharacterClass.UNKNOWN,
                LoginStatus.UNKNOWN,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                Faction.VS,
                Server("", Namespace.PS2PC, "", null),
                null,
                null,
                Namespace.PS2PS4EU,
                Instant.DISTANT_PAST,
                false,
            ),
            preferredOutfit = Outfit(
                id = "",
                name = null,
                tag = "D3RP",
                faction = Faction.VS,
                timeCreated = null,
                leader = null,
                memberCount = 100,
                cached = false,
                namespace = Namespace.PS2PC,
            ),
            eventHandler = object : MainMenuEventHandler {
                override fun onPreferredProfileClick(characterId: String, namespace: Namespace) = Unit
                override fun onPreferredOutfitClick(outfitId: String, namespace: Namespace) = Unit
                override fun onProfileClick() = Unit
                override fun onServersClick() = Unit
                override fun onOutfitsClick() = Unit
                override fun onTwitterClick() = Unit
                override fun onRedditClick() = Unit
                override fun onAboutClick() = Unit
            },
        )
    }
}
