package com.cesarandres.ps2link.fragments.redditpager

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.appfrontend.redditpager.RedditCompose
import com.cramsan.ps2link.appfrontend.redditpager.RedditEventHandler
import com.cramsan.ps2link.appfrontend.redditpager.RedditPostUIModel
import com.cramsan.ps2link.ui.theme.PS2Theme
import kotlinx.collections.immutable.persistentListOf

@Preview
@Composable
fun Preview() {
    PS2Theme {
        RedditCompose(
            redditContent = persistentListOf(),
            isLoading = true,
            isError = true,
            eventHandler = object : RedditEventHandler {
                override fun onPostSelected(redditPost: RedditPostUIModel) = Unit
                override fun onImageSelected(redditPost: RedditPostUIModel) = Unit
                override fun onRefreshRequested() = Unit
            },
        )
    }
}
