package com.cramsan.ps2link.appfrontend.twitter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cramsan.ps2link.appfrontend.UnknownErrorText
import com.cramsan.ps2link.ui.ErrorOverlay
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.FrameSlim
import com.cramsan.ps2link.ui.SwipeToRefresh
import com.cramsan.ps2link.ui.items.PS2TweetUIModel
import com.cramsan.ps2link.ui.items.TweetItem
import com.cramsan.ps2link.ui.theme.Padding
import kotlinx.collections.immutable.ImmutableList

@Suppress("FunctionNaming", "UndocumentedPublicFunction")
@Composable
fun TweetListCompose(
    users: Map<String, Boolean>,
    tweetItems: ImmutableList<PS2TweetUIModel>,
    isLoading: Boolean,
    isError: Boolean,
    eventHandler: TweetListComposeEventHandler,
) {
    FrameBottom {
        Column {
            FrameSlim {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    LazyRow {
                        items(users.toList()) { pair ->
                            Row(
                                modifier = Modifier.clickable {
                                    eventHandler.onTwitterUserClicked(pair.first)
                                }.padding(vertical = Padding.medium, horizontal = Padding.large),
                            ) {
                                Checkbox(
                                    modifier = Modifier.padding(horizontal = Padding.small),
                                    checked = pair.second,
                                    onCheckedChange = null,
                                )
                                Text(
                                    text = TwitterHandleText(pair.first),
                                )
                            }
                        }
                    }
                }
            }
            Box(modifier = Modifier.fillMaxSize()) {
                SwipeToRefresh(
                    isLoading = isLoading,
                    onRefreshRequested = { eventHandler.onRefreshRequested() },
                ) {
                    items(tweetItems) { tweet ->
                        TweetItem(
                            model = tweet,
                            onClick = { eventHandler.onTweetSelected(tweet) },
                        )
                    }
                }
                ErrorOverlay(
                    isError = isError,
                    error = UnknownErrorText()
                )
            }
        }
    }
}

interface TweetListComposeEventHandler {
    @Suppress("UndocumentedPublicFunction")
    fun onTwitterUserClicked(twitterUser: String)

    /**
     * User has clicked on tweet.
     */
    fun onTweetSelected(tweet: PS2TweetUIModel)

    fun onRefreshRequested()
}

@Composable
expect fun TwitterHandleText(handle: String): String
