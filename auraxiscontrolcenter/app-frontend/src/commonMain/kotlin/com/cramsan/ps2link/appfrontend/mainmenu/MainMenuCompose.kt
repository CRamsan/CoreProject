package com.cramsan.ps2link.appfrontend.mainmenu

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.core.models.Outfit
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.MainMenuButton

@Composable
fun MainMenuCompose(
    preferredProfile: Character?,
    preferredOutfit: Outfit?,
    eventHandler: MainMenuEventHandler,
) {
    FrameBottom {
        Column(
            modifier = Modifier
                .padding(horizontal = 50.dp)
                .verticalScroll(rememberScrollState())
                .wrapContentWidth()
                .animateContentSize(),
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            val buttonModifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
            AnimatedVisibility(preferredProfile != null) {
                MainMenuButton(
                    buttonModifier,
                    label = preferredProfile?.name,
                    star = true,
                ) {
                    preferredProfile?.let {
                        eventHandler.onPreferredProfileClick(it.characterId, it.namespace)
                    }
                }
            }
            AnimatedVisibility(preferredOutfit != null) {
                MainMenuButton(
                    buttonModifier,
                    label = preferredOutfit?.name,
                    star = true,
                ) {
                    preferredOutfit?.let {
                        eventHandler.onPreferredOutfitClick(it.id, it.namespace)
                    }
                }
            }
            MainMenuButton(
                buttonModifier,
                label = TitleProfilesText(),
                star = false,
            ) { eventHandler.onProfileClick() }
            MainMenuButton(
                buttonModifier,
                label = TitleServerText(),
                star = false,
            ) { eventHandler.onServersClick() }
            MainMenuButton(
                buttonModifier,
                label = TitleOutfitsText(),
                star = false,
            ) { eventHandler.onOutfitsClick() }
            MainMenuButton(
                buttonModifier,
                label = TitleRedditText(),
                star = false,
            ) { eventHandler.onRedditClick() }
            MainMenuButton(
                buttonModifier,
                label = TitleAboutText(),
                star = false,
            ) { eventHandler.onAboutClick() }
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

interface MainMenuEventHandler {
    fun onPreferredProfileClick(characterId: String, namespace: Namespace)
    fun onPreferredOutfitClick(outfitId: String, namespace: Namespace)
    fun onProfileClick()
    fun onServersClick()
    fun onOutfitsClick()
    fun onTwitterClick()
    fun onRedditClick()
    fun onAboutClick()
}

@Composable
expect fun TitleProfilesText(): String

@Composable
expect fun TitleServerText(): String

@Composable
expect fun TitleOutfitsText(): String

@Composable
expect fun TitleTwitterText(): String

@Composable
expect fun TitleRedditText(): String

@Composable
expect fun TitleAboutText(): String
