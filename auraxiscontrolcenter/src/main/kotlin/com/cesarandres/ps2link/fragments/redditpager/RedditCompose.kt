package com.cesarandres.ps2link.fragments.redditpager

import androidx.annotation.MainThread
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.core.models.RedditPost
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.LoadingOverlay
import com.cramsan.ps2link.ui.items.RedditPostItem
import com.cramsan.ps2link.ui.theme.PS2Theme
import org.ocpsoft.prettytime.PrettyTime

@Composable
fun RedditCompose(
    redditContent: List<RedditPost>,
    isLoading: Boolean,
    prettyTime: PrettyTime,
    eventHandler: RedditEventHandler,
) {
    FrameBottom(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            items(redditContent) {
                RedditPostItem(
                    modifier = Modifier.fillParentMaxWidth(),
                    imgUrl = it.imgUr,
                    title = it.title,
                    author = it.author,
                    upvotes = it.upvotes,
                    comments = it.comments,
                    createdTime = it.createdTime,
                    prettyTime = prettyTime,
                    onImageClick = { eventHandler.onImageSelected(it) },
                    onPostClick = { eventHandler.onPostSelected(it) },
                )
            }
        }
        LoadingOverlay(enabled = isLoading)
    }
}

@MainThread
interface RedditEventHandler {
    fun onPostSelected(redditPost: RedditPost)
    fun onImageSelected(redditPost: RedditPost)
}

@Preview
@Composable
fun Preview() {
    PS2Theme {
        RedditCompose(
            redditContent = emptyList(),
            isLoading = true,
            prettyTime = PrettyTime(),
            eventHandler = object : RedditEventHandler {
                override fun onPostSelected(redditPost: RedditPost) = Unit
                override fun onImageSelected(redditPost: RedditPost) = Unit
            },
        )
    }
}
