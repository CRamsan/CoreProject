package com.cesarandres.ps2link.fragments.about

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.appfrontend.about.AboutCompose
import com.cramsan.ps2link.appfrontend.about.AboutEventHandler

@Preview(
    widthDp = 900,
    heightDp = 400,

)
@Composable
fun AboutPreview() {
    AboutCompose(
        eventHandler = object : AboutEventHandler {
            override fun onAboutClick() = Unit
        },
    )
}
