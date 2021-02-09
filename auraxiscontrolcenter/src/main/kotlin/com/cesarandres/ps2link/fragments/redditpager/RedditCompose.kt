package com.cesarandres.ps2link.fragments.redditpager

import androidx.annotation.MainThread
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.core.models.RedditPost
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.items.RedditPostItem

@Composable
fun RedditCompose(
    redditContent: List<RedditPost>,
    isLoading: Boolean,
    eventHandler: RedditEventHandler,
) {
    FrameBottom(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn {
                items(redditContent) {
                    RedditPostItem(
                        label = it.title ?: "",
                        onClick = { eventHandler.onPostSelected(it) }
                    )
                }
            }
            if (isLoading) {
                CircularProgressIndicator()
            }
        }
    }
}

@MainThread
interface RedditEventHandler {
    fun onPostSelected(redditPost: RedditPost)
}

@Preview
@Composable
fun Preview() {
    RedditCompose(
        redditContent = emptyList(),
        isLoading = true,
        eventHandler = object : RedditEventHandler {
            override fun onPostSelected(redditPost: RedditPost) = Unit
        },
    )
}
