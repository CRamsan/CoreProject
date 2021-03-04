package com.cramsan.ps2link.ui.items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.ui.SlimButton
import com.cramsan.ps2link.ui.theme.PS2Theme
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
                Text(
                    text = upvotes.toString(),
                )
            }
            Column {
                Text(
                    text = title,
                )
                Row {
                    Text(
                        text = author,
                    )
                }
                val creationTime = prettyTime.format(Date(createdTime))
                Text(
                    text = creationTime,
                )
                Text(
                    text = comments.toString(),
                )
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
