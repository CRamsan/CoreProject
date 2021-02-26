package com.cesarandres.ps2link.fragments.about

import androidx.annotation.MainThread
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
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.MainMenuButton

@Composable
fun AboutCompose(
    eventHandler: AboutEventHandler,
) {
    FrameBottom(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.padding(horizontal = 50.dp)
                .verticalScroll(rememberScrollState())
                .wrapContentWidth()
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            val buttonModifier = Modifier.padding(10.dp).fillMaxWidth()
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
interface AboutEventHandler {
    fun onAboutClick()
}

@Preview
@Composable
fun AboutPreview() {
    AboutCompose(
        eventHandler = object : AboutEventHandler {
            override fun onAboutClick() = Unit
        }
    )
}
