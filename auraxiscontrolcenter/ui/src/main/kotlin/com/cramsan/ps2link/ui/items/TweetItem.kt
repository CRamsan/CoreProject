package com.cramsan.ps2link.ui.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.cramsan.ps2link.ui.R
import com.cramsan.ps2link.ui.SlimButton
import com.cramsan.ps2link.ui.theme.PS2Theme
import com.cramsan.ps2link.ui.theme.Padding
import com.cramsan.ps2link.ui.theme.Size
import org.ocpsoft.prettytime.PrettyTime
import java.util.Date

@OptIn(ExperimentalCoilApi::class)
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
                Text(username)
                Text(
                    modifier = Modifier.weight(1f).padding(horizontal = Padding.small).align(Alignment.CenterVertically),
                    text = stringResource(
                        id = R.string.title_twitter_handle,
                        formatArgs = arrayOf(handle)
                    ),
                    style = MaterialTheme.typography.caption,
                )
                val updateTime = prettyTime.format(Date(creationTime))
                Text(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    text = updateTime,
                    style = MaterialTheme.typography.overline,
                )
            }
            Row {
                Box(
                    modifier = Modifier.size(Size.xxlarge).padding(Padding.small).align(CenterVertically)
                ) {
                    val painter = rememberImagePainter(
                        data = avatarUrl,
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
                            CircularProgressIndicator(Modifier.align(Alignment.Center))
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
                Text(
                    modifier = Modifier.padding(vertical = Padding.medium),
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
            avatarUrl = "",
            prettyTime = PrettyTime(),
            creationTime = 19993932,
        )
    }
}
