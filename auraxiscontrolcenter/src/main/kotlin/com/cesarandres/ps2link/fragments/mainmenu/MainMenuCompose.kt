package com.cesarandres.ps2link.fragments.mainmenu

import androidx.annotation.MainThread
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cesarandres.ps2link.R
import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.core.models.Outfit
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.MainMenuButton
import com.cramsan.ps2link.ui.theme.PS2Theme

@Composable
fun MainMenuCompose(
    preferredProfile: Character?,
    preferredOutfit: Outfit?,
    eventHandler: MainMenuEventHandler,
) {
    FrameBottom(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.padding(horizontal = 50.dp)
                .verticalScroll(rememberScrollState())
                .wrapContentWidth()
                .animateContentSize()
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            val buttonModifier = Modifier.padding(10.dp).fillMaxWidth()
            preferredProfile?.let {
                MainMenuButton(
                    buttonModifier,
                    label = it.name ?: "",
                    star = true
                ) { eventHandler.onPreferredProfileClick(it.characterId, it.namespace) }
            }
            preferredOutfit?.let {
                MainMenuButton(
                    buttonModifier,
                    label = it.name ?: "",
                    star = true
                ) { eventHandler.onPreferredOutfitClick(it.id, it.namespace) }
            }
            MainMenuButton(
                buttonModifier,
                label = stringResource(R.string.title_profiles),
                star = false,
            ) { eventHandler.onProfileClick() }
            MainMenuButton(
                buttonModifier,
                label = stringResource(R.string.title_servers),
                star = false,
            ) { eventHandler.onServersClick() }
            MainMenuButton(
                buttonModifier,
                label = stringResource(R.string.title_outfits),
                star = false,
            ) { eventHandler.onOutfitsClick() }
            MainMenuButton(
                buttonModifier,
                label = stringResource(R.string.title_twitter),
                star = false,
            ) { eventHandler.onTwitterClick() }
            MainMenuButton(
                buttonModifier,
                label = stringResource(R.string.title_reddit),
                star = false,
            ) { eventHandler.onRedditClick() }
            MainMenuButton(
                buttonModifier,
                label = stringResource(R.string.title_about),
                star = false,
            ) { eventHandler.onAboutClick() }
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

@MainThread
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

@Preview(
    showBackground = true,
    backgroundColor = 0xFF000000,
)
@Composable
fun NormalButtonPreview() {
    PS2Theme {
        MainMenuCompose(
            preferredProfile = null,
            preferredOutfit = null,
            eventHandler = object : MainMenuEventHandler {
                override fun onPreferredProfileClick(characterId: String, namespace: Namespace) = Unit
                override fun onPreferredOutfitClick(outfitId: String, namespace: Namespace) = Unit
                override fun onProfileClick() = Unit
                override fun onServersClick() = Unit
                override fun onOutfitsClick() = Unit
                override fun onTwitterClick() = Unit
                override fun onRedditClick() = Unit
                override fun onAboutClick() = Unit
            }
        )
    }
}
