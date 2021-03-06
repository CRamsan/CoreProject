package com.cramsan.ps2link.ui.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.ui.R
import com.cramsan.ps2link.ui.SlimButton
import com.cramsan.ps2link.ui.theme.PS2Theme
import com.cramsan.ps2link.ui.theme.Padding
import com.cramsan.ps2link.ui.theme.Size
import org.ocpsoft.prettytime.PrettyTime
import java.util.Date

@Composable
fun RedditPostItem(
    modifier: Modifier = Modifier,
    url: String,
    title: String,
    author: String,
    upvotes: Int,
    comments: Int,
    createdTime: Long,
    prettyTime: PrettyTime,
    onClick: () -> Unit = {},
) {
    SlimButton(
        modifier = modifier
    ) {
        Row {
            Column {
                Image(
                    modifier = Modifier.size(Size.xlarge),
                    painter = painterResource(id = R.drawable.icon_faction_vs),
                    contentDescription = null
                )
                Text(
                    text = upvotes.toString(),
                )
            }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    modifier = Modifier.padding(Padding.small),
                    text = title,
                    style = MaterialTheme.typography.body1,
                )
                Row {
                    val creationTime = prettyTime.format(Date(createdTime))
                    Text(
                        modifier = Modifier.padding(Padding.small),
                        text = stringResource(id = R.string.twitter_upload_by, formatArgs = arrayOf(creationTime, author)),
                        style = MaterialTheme.typography.body2,
                    )
                }
                Row {
                    Text(
                        modifier = Modifier.padding(Padding.small).wrapContentSize(),
                        text = stringResource(id = R.string.twitter_comments, formatArgs = arrayOf(comments)),
                        style = MaterialTheme.typography.body2,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun RedditPostItemPreview() {
    PS2Theme {
        RedditPostItem(
            url = "",
            title = "",
            author = "",
            upvotes = 30,
            comments = 100,
            createdTime = 13425432,
            prettyTime = PrettyTime(),
        )
    }
}
