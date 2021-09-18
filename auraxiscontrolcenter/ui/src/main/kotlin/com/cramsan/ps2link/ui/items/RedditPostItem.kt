package com.cramsan.ps2link.ui.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.cramsan.ps2link.ui.R
import com.cramsan.ps2link.ui.SlimButton
import com.cramsan.ps2link.ui.theme.PS2Theme
import com.cramsan.ps2link.ui.theme.Padding
import org.ocpsoft.prettytime.PrettyTime
import java.util.Date

@OptIn(ExperimentalCoilApi::class)
@Composable
fun RedditPostItem(
    modifier: Modifier = Modifier,
    imgUrl: String?,
    title: String,
    author: String,
    upvotes: Int,
    comments: Int,
    createdTime: Long,
    prettyTime: PrettyTime,
    onImageClick: () -> Unit = {},
    onPostClick: () -> Unit = {},
) {
    SlimButton(
        modifier = modifier,
        onClick = onPostClick,
    ) {
        Row {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.body1,
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis,
                )
                val creationTime = prettyTime.format(Date(createdTime))
                Text(
                    modifier = Modifier.padding(top = Padding.xsmall),
                    text = stringResource(id = R.string.twitter_upload_by, formatArgs = arrayOf(creationTime, author)),
                    style = MaterialTheme.typography.caption,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = stringResource(id = R.string.twitter_comments, formatArgs = arrayOf(comments)),
                    style = MaterialTheme.typography.overline,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Column(
                modifier = Modifier.wrapContentWidth().padding(start = Padding.small).align(
                    Alignment.CenterVertically
                )
            ) {
                Box(
                    modifier = Modifier.size(imageWidth, imageHeight).clickable {
                        onImageClick()
                    }
                ) {

                    if (imgUrl == null) {
                        Image(
                            modifier = Modifier.fillMaxSize(),
                            painter = painterResource(id = R.drawable.image_not_found),
                            contentScale = ContentScale.Fit,
                            contentDescription = null
                        )
                    } else {
                        val painter = rememberImagePainter(
                            data = imgUrl,
                            builder = {
                                crossfade(true)
                                placeholder(R.drawable.image_not_found)
                            }
                        )
                        Image(
                            modifier = Modifier.fillMaxSize(),
                            painter = painter,
                            contentScale = ContentScale.Crop,
                            contentDescription = null
                        )
                        when (painter.state) {
                            is ImagePainter.State.Loading -> {
                                // Display a circular progress indicator whilst loading
                                CircularProgressIndicator(Modifier.align(Center))
                            }
                            is ImagePainter.State.Error, ImagePainter.State.Empty -> {
                                Image(
                                    modifier = Modifier.fillMaxSize(),
                                    painter = painterResource(id = R.drawable.image_not_found),
                                    contentScale = ContentScale.Fit,
                                    contentDescription = null
                                )
                            }
                            else -> Unit
                        }
                    }
                }
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

@Preview
@Composable
fun RedditPostItemPreview() {
    PS2Theme {
        RedditPostItem(
            imgUrl = "",
            title = "This is an example of a reddit post for new changes coming to Planetside.",
            author = "ThisIsNoTAnExampleOfUser",
            upvotes = 30,
            comments = 100,
            createdTime = 13425432,
            prettyTime = PrettyTime(),
        )
    }
}
