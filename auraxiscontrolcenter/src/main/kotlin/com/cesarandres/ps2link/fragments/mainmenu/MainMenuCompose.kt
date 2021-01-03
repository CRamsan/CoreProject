package com.cesarandres.ps2link.fragments.mainmenu

import androidx.annotation.MainThread
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cesarandres.ps2link.R
import com.cramsan.ps2link.ui.BoldButton

@Composable
fun MainMenuCompose(
    preferredProfile: String?,
    preferredOutfit: String?,
    eventHandler: MainMenuEventHandler,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        ScrollableColumn(
            modifier = Modifier.padding(horizontal = 50.dp)
                .wrapContentWidth()
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            val buttonModifier = Modifier.padding(10.dp).fillMaxWidth()
            preferredProfile?.let {
                BoldButton(buttonModifier, it, true) { eventHandler.onPreferredProfileClick() }
            }
            preferredOutfit?.let {
                BoldButton(buttonModifier, it, true) { eventHandler.onPreferredOutfitClick() }
            }
            BoldButton(
                buttonModifier,
                stringResource(R.string.title_profiles), false,
            ) { eventHandler.onProfileClick() }
            BoldButton(
                buttonModifier,
                stringResource(R.string.title_servers), false,
            ) { eventHandler.onServersClick() }
            BoldButton(
                buttonModifier,
                stringResource(R.string.title_outfits), false,
            ) { eventHandler.onOutfitsClick() }
            BoldButton(
                buttonModifier,
                stringResource(R.string.title_twitter), false,
            ) { eventHandler.onTwitterClick() }
            BoldButton(
                buttonModifier,
                stringResource(R.string.title_reddit), false,
            ) { eventHandler.onRedditClick() }
            BoldButton(
                buttonModifier,
                stringResource(R.string.title_about), false,
            ) { eventHandler.onAboutClick() }
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

@MainThread
interface MainMenuEventHandler {
    fun onPreferredProfileClick()
    fun onPreferredOutfitClick()
    fun onProfileClick()
    fun onServersClick()
    fun onOutfitsClick()
    fun onTwitterClick()
    fun onRedditClick()
    fun onAboutClick()
}

@Preview
@Composable
fun NormalButtonPreview() {
    MainMenuCompose(
        preferredProfile = "Cramsan",
        preferredOutfit = "Derp Company",
        eventHandler = object : MainMenuEventHandler {
            override fun onPreferredProfileClick() = Unit
            override fun onPreferredOutfitClick() = Unit
            override fun onProfileClick() = Unit
            override fun onServersClick() = Unit
            override fun onOutfitsClick() = Unit
            override fun onTwitterClick() = Unit
            override fun onRedditClick() = Unit
            override fun onAboutClick() = Unit
        }
    )
}
