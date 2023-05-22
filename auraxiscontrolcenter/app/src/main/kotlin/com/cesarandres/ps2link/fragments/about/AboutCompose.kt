package com.cesarandres.ps2link.fragments.about

import androidx.annotation.MainThread
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.cesarandres.ps2link.R
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.FrameSlim
import com.cramsan.ps2link.ui.theme.Padding

@Composable
fun AboutCompose(
    eventHandler: AboutEventHandler,
) {
    FrameBottom {
        Column(
            modifier = Modifier
                .padding(horizontal = Padding.xxlarge)
                .wrapContentWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(Padding.xxlarge))
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.h6,
            )
            Text(text = stringResource(com.cramsan.ps2link.ui.R.string.text_developed_by))
            Spacer(modifier = Modifier.height(Padding.xxlarge))
            FrameSlim {
                Text(
                    text = stringResource(com.cramsan.ps2link.ui.R.string.text_about_description),
                    modifier = Modifier.padding(Padding.medium),
                    style = MaterialTheme.typography.caption,
                )
            }
            Spacer(modifier = Modifier.height(Padding.xlarge))
            FrameSlim(
                modifier = Modifier.clickable {
                    eventHandler.onAboutClick()
                },
            ) {
                Text(
                    text = stringResource(com.cramsan.ps2link.ui.R.string.url_homepage),
                    modifier = Modifier.padding(Padding.medium),
                )
            }
            Spacer(modifier = Modifier.height(Padding.xlarge))
            FrameSlim {
                Text(
                    text = stringResource(com.cramsan.ps2link.ui.R.string.text_about_thanks),
                    modifier = Modifier.padding(Padding.medium),
                    style = MaterialTheme.typography.caption,
                )
            }
        }
    }
}

@MainThread
interface AboutEventHandler {
    fun onAboutClick()
}

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
