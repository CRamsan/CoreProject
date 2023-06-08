package com.cramsan.ps2link.appfrontend.about

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
                text = getAppName(),
                style = MaterialTheme.typography.h6,
            )
            Text(text = getDevelopedBy())
            Spacer(modifier = Modifier.height(Padding.xxlarge))
            FrameSlim {
                Text(
                    text = getAboutDescription(),
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
                    text = getUrlHomePage(),
                    modifier = Modifier.padding(Padding.medium),
                )
            }
            Spacer(modifier = Modifier.height(Padding.xlarge))
            FrameSlim {
                Text(
                    text = getAboutThankYou(),
                    modifier = Modifier.padding(Padding.medium),
                    style = MaterialTheme.typography.caption,
                )
            }
        }
    }
}

@Composable
expect fun getAppName(): String

@Composable
expect fun getDevelopedBy(): String

@Composable
expect fun getAboutDescription(): String

@Composable
expect fun getUrlHomePage(): String

@Composable
expect fun getAboutThankYou(): String

interface AboutEventHandler {
    fun onAboutClick()
}
