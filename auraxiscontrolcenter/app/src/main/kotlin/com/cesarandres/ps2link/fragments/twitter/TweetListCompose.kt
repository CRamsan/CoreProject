package com.cesarandres.ps2link.fragments.twitter

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.appfrontend.twitter.TweetListCompose
import com.cramsan.ps2link.appfrontend.twitter.TweetListComposeEventHandler
import com.cramsan.ps2link.ui.items.PS2TweetUIModel
import com.cramsan.ps2link.ui.theme.PS2Theme
import kotlinx.collections.immutable.persistentListOf

@Preview
@Composable
fun ServerListPreview() {
    PS2Theme {
        TweetListCompose(
            tweetItems = persistentListOf(),
            users = emptyMap(),
            isLoading = false,
            isError = false,
            eventHandler = object : TweetListComposeEventHandler {
                override fun onTwitterUserClicked(twitterUser: String) = Unit
                override fun onTweetSelected(tweet: PS2TweetUIModel) = Unit
                override fun onRefreshRequested() = Unit
            },
        )
    }
}
