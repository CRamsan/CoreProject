package com.cramsan.ps2link.ui.items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.ui.R
import com.cramsan.ps2link.ui.SlimButton
import com.cramsan.ps2link.ui.theme.PS2Theme
import com.cramsan.ps2link.ui.theme.Padding
import com.cramsan.ps2link.ui.theme.Size
import com.cramsan.ps2link.ui.widgets.NetworkImage
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import org.ocpsoft.prettytime.PrettyTime

/**
 * Renders a single tweet.
 */
@Composable
@Suppress("FunctionNaming")
fun TweetItem(
    modifier: Modifier = Modifier,
    username: String,
    handle: String,
    content: String,
    avatarUrl: String,
    creationTime: Instant,
    prettyTime: PrettyTime,
    onClick: () -> Unit = {},
) {
    SlimButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        Column {
            Row {
                Text(username)
                Text(
                    modifier = Modifier.weight(1f).padding(horizontal = Padding.small).align(CenterVertically),
                    text = stringResource(
                        id = R.string.title_twitter_handle,
                        formatArgs = arrayOf(handle),
                    ),
                    style = MaterialTheme.typography.caption,
                )
                val updateTime = prettyTime.format(creationTime.toJavaInstant())
                Text(
                    modifier = Modifier.align(CenterVertically),
                    text = updateTime,
                    style = MaterialTheme.typography.overline,
                )
            }
            Row {
                NetworkImage(
                    modifier = Modifier.size(Size.xxlarge)
                        .padding(Padding.small)
                        .align(CenterVertically),
                    imageUrl = avatarUrl,
                )
                Text(
                    modifier = Modifier.weight(1f)
                        .padding(vertical = Padding.medium),
                    text = content,
                )
            }
        }
    }
}

@Preview
@Composable
fun TweetItemPreview() {
    PS2Theme {
        TweetItem(
            username = "Planetside",
            handle = "planetside",
            content = "This is an example of content\nin multiple lines\nbye",
            avatarUrl = "https://census.daybreakgames.com/files/ps2/images/static/88685.png",
            prettyTime = PrettyTime(),
            creationTime = Instant.fromEpochSeconds(19993932),
        )
    }
}
