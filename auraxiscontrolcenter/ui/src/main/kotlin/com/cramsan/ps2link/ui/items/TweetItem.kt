package com.cramsan.ps2link.ui.items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.ui.R
import com.cramsan.ps2link.ui.SlimButton
import com.cramsan.ps2link.ui.theme.PS2Theme
import com.cramsan.ps2link.ui.theme.Padding
import org.ocpsoft.prettytime.PrettyTime
import java.util.Date

@Composable
fun TweetItem(
    modifier: Modifier = Modifier,
    username: String,
    handle: String,
    content: String,
    avatarUrl: String,
    creationTime: Long,
    prettyTime: PrettyTime,
) {
    SlimButton(
        modifier = modifier
    ) {
        Column {
            Row {
                Text(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    text = username,
                    style = MaterialTheme.typography.h6,
                )
                Text(
                    modifier = Modifier.weight(1f).padding(horizontal = Padding.small).align(Alignment.CenterVertically),
                    text = stringResource(
                        id = R.string.title_twitter_handle,
                        formatArgs = arrayOf(handle)
                    ),
                    style = MaterialTheme.typography.subtitle2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                val updateTime = prettyTime.format(Date(creationTime))
                Text(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    text = updateTime,
                    style = MaterialTheme.typography.subtitle2,
                )
            }
            Row {
                /*
                CoilImage(
                    modifier = Modifier.size(Size.xlarge),
                    data = avatarUrl,
                    contentDescription = username,
                    fadeIn = true,
                )
                Text(
                    modifier = Modifier.padding(vertical = Padding.medium),
                    text = content,
                )
                 */
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
            content = "This is an example of content",
            avatarUrl = "",
            prettyTime = PrettyTime(),
            creationTime = 19993932,
        )
    }
}
