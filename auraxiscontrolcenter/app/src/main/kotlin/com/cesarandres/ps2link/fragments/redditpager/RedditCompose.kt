package com.cesarandres.ps2link.fragments.redditpager

import androidx.annotation.MainThread
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.core.models.RedditPost
import com.cramsan.ps2link.ui.ErrorOverlay
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.R
import com.cramsan.ps2link.ui.SwipeToRefresh
import com.cramsan.ps2link.ui.items.RedditPostItem
import com.cramsan.ps2link.ui.theme.PS2Theme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.toJavaInstant
import org.ocpsoft.prettytime.PrettyTime

/**
 * Render the list of posts for a given subreddit.
 */
@Composable
fun RedditCompose(
    redditContent: ImmutableList<RedditPost>,
    isLoading: Boolean,
    isError: Boolean,
    prettyTime: PrettyTime,
    eventHandler: RedditEventHandler,
) {
    FrameBottom {
        SwipeToRefresh(
            isLoading = isLoading,
            onRefreshRequested = { eventHandler.onRefreshRequested() },
        ) {
            items(redditContent) {
                val creationTime = prettyTime.format(it.createdTime.toJavaInstant())
                val subText =
                    stringResource(id = R.string.twitter_upload_by, formatArgs = arrayOf(creationTime, it.author))
                RedditPostItem(
                    modifier = Modifier.fillParentMaxWidth(),
                    imgUrl = it.imgUr,
                    title = it.title,
                    subtext = subText,
                    upvotes = it.upvotes,
                    comments = stringResource(id = R.string.twitter_comments, formatArgs = arrayOf(it.comments)),
                    onImageClick = { eventHandler.onImageSelected(it) },
                    onPostClick = { eventHandler.onPostSelected(it) },
                )
            }
        }
        ErrorOverlay(isError = isError, error = stringResource(id = R.string.text_unkown_error))
    }
}

@MainThread
interface RedditEventHandler {
    fun onPostSelected(redditPost: RedditPost)
    fun onImageSelected(redditPost: RedditPost)
    fun onRefreshRequested()
}

@Preview
@Composable
fun Preview() {
    PS2Theme {
        RedditCompose(
            redditContent = persistentListOf(),
            isLoading = true,
            isError = true,
            prettyTime = PrettyTime(),
            eventHandler = object : RedditEventHandler {
                override fun onPostSelected(redditPost: RedditPost) = Unit
                override fun onImageSelected(redditPost: RedditPost) = Unit
                override fun onRefreshRequested() = Unit
            },
        )
    }
}
