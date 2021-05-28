package com.cesarandres.ps2link.fragments.twitter

import androidx.annotation.MainThread
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.cesarandres.ps2link.R
import com.cramsan.ps2link.appcore.twitter.TwitterUser
import com.cramsan.ps2link.core.models.PS2Tweet
import com.cramsan.ps2link.ui.ErrorOverlay
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.FrameSlim
import com.cramsan.ps2link.ui.LoadingOverlay
import com.cramsan.ps2link.ui.items.TweetItem
import com.cramsan.ps2link.ui.theme.PS2Theme
import com.cramsan.ps2link.ui.theme.Padding
import org.ocpsoft.prettytime.PrettyTime

@Composable
fun TweetListCompose(
    users: Map<TwitterUser, Boolean>,
    tweetItems: List<PS2Tweet>,
    isLoading: Boolean,
    isError: Boolean,
    prettyTime: PrettyTime,
    eventHandler: TweetListComposeEventHandler,
) {
    FrameBottom {
        Column {
            FrameSlim {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    LazyRow {
                        items(users.toList()) { pair ->
                            Row(
                                modifier = Modifier.clickable {
                                    eventHandler.onTwitterUserClicked(pair.first)
                                }.padding(vertical = Padding.medium, horizontal = Padding.large)
                            ) {
                                Checkbox(
                                    modifier = Modifier.padding(horizontal = Padding.small),
                                    checked = pair.second,
                                    onCheckedChange = null
                                )
                                Text(
                                    text = stringResource(
                                        id = R.string.title_twitter_handle,
                                        formatArgs = arrayOf(pair.first.handle)
                                    )
                                )
                            }
                        }
                    }
                }
            }
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn {
                    items(tweetItems) {
                        TweetItem(
                            username = it.user,
                            handle = it.tag,
                            content = it.content,
                            avatarUrl = it.imgUrl,
                            prettyTime = prettyTime,
                            creationTime = it.date,
                        )
                    }
                }
                LoadingOverlay(enabled = isLoading)
                ErrorOverlay(isError = isError)
            }
        }
    }
}

@MainThread
interface TweetListComposeEventHandler {
    fun onTwitterUserClicked(twitterUser: TwitterUser)
}

@Preview
@Composable
fun ServerListPreview() {
    PS2Theme {
        TweetListCompose(
            tweetItems = emptyList(),
            users = emptyMap(),
            isLoading = false,
            isError = false,
            prettyTime = PrettyTime(),
            eventHandler = object : TweetListComposeEventHandler {
                override fun onTwitterUserClicked(twitterUser: TwitterUser) = Unit
            }
        )
    }
}
