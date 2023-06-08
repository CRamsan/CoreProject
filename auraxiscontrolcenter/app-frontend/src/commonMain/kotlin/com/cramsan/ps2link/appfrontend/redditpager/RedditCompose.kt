package com.cramsan.ps2link.appfrontend.redditpager

import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cramsan.ps2link.appfrontend.UnknownErrorText
import com.cramsan.ps2link.ui.ErrorOverlay
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.SwipeToRefresh
import com.cramsan.ps2link.ui.items.RedditPostItem
import kotlinx.collections.immutable.ImmutableList

/**
 * Render the list of posts for a given subreddit.
 */
@Composable
fun RedditCompose(
    redditContent: ImmutableList<RedditPostUIModel>,
    isLoading: Boolean,
    isError: Boolean,
    eventHandler: RedditEventHandler,
) {
    FrameBottom {
        SwipeToRefresh(
            isLoading = isLoading,
            onRefreshRequested = { eventHandler.onRefreshRequested() },
        ) {
            items(redditContent) {
                val creationTime = it.createdTime
                val subText =
                    TwitterUploadedBy(creationTime, it.author)
                RedditPostItem(
                    modifier = Modifier.fillParentMaxWidth(),
                    imgUrl = it.imgUr,
                    title = it.title,
                    subtext = subText,
                    upvotes = it.upvotes,
                    comments = TwitterComments(it.comments),
                    onImageClick = { eventHandler.onImageSelected(it) },
                    onPostClick = { eventHandler.onPostSelected(it) },
                )
            }
        }
        ErrorOverlay(isError = isError, error = UnknownErrorText())
    }
}

interface RedditEventHandler {
    fun onPostSelected(redditPost: RedditPostUIModel)
    fun onImageSelected(redditPost: RedditPostUIModel)
    fun onRefreshRequested()
}

data class RedditPostUIModel(
    val url: String?,
    val title: String,
    val imgUr: String?,
    val author: String,
    val label: String?,
    val upvotes: Int,
    val comments: Int,
    val createdTime: String,
    val postUrl: String?,
)

@Composable
expect fun TwitterUploadedBy(createdTime: String, author: String): String

@Composable
expect fun TwitterComments(count: Int): String
