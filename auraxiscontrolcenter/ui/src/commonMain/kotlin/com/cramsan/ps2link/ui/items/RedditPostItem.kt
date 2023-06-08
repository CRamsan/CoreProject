package com.cramsan.ps2link.ui.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.cramsan.ps2link.ui.SlimButton
import com.cramsan.ps2link.ui.theme.Padding
import com.cramsan.ps2link.ui.widgets.NetworkImage

/**
 * Render a Reddit post.
 */
@Composable
fun RedditPostItem(
    modifier: Modifier = Modifier,
    imgUrl: String?,
    title: String,
    subtext: String,
    upvotes: Int,
    comments: String,
    onImageClick: () -> Unit = {},
    onPostClick: () -> Unit = {},
) {
    SlimButton(
        modifier = modifier,
        onClick = onPostClick,
    ) {
        Row {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.body1,
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    modifier = Modifier.padding(top = Padding.xsmall),
                    text = subtext,
                    style = MaterialTheme.typography.caption,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = comments,
                    style = MaterialTheme.typography.overline,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Column(
                modifier = Modifier.wrapContentWidth().padding(start = Padding.small).align(
                    Alignment.CenterVertically,
                ),
            ) {
                NetworkImage(
                    modifier = Modifier.size(imageWidth, imageHeight).clickable {
                        onImageClick()
                    },
                    imageUrl = imgUrl,
                    contentScale = ContentScale.Fit,
                )
                Text(
                    modifier = Modifier.align(CenterHorizontally),
                    text = upvotes.toString(),
                    style = MaterialTheme.typography.caption,
                )
            }
        }
    }
}

private val imageWidth = 100.dp
private val imageHeight = 56.dp
