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
import com.cramsan.ps2link.ui.SlimButton
import com.cramsan.ps2link.ui.theme.Padding
import com.cramsan.ps2link.ui.theme.Size
import com.cramsan.ps2link.ui.widgets.NetworkImage

/**
 * Renders a single tweet.
 */
@Composable
@Suppress("FunctionNaming")
fun TweetItem(
    modifier: Modifier = Modifier,
    model: PS2TweetUIModel,
    onClick: () -> Unit = {},
) {
    SlimButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        Column {
            Row {
                Text(model.user)
                Text(
                    modifier = Modifier.weight(1f).padding(horizontal = Padding.small).align(CenterVertically),
                    text = model.tag,
                    style = MaterialTheme.typography.caption,
                )
                Text(
                    modifier = Modifier.align(CenterVertically),
                    text = model.creationTime,
                    style = MaterialTheme.typography.overline,
                )
            }
            Row {
                NetworkImage(
                    modifier = Modifier.size(Size.xxlarge)
                        .padding(Padding.small)
                        .align(CenterVertically),
                    imageUrl = model.imgUrl,
                )
                Text(
                    modifier = Modifier.weight(1f)
                        .padding(vertical = Padding.medium),
                    text = model.content,
                )
            }
        }
    }
}

/**
 * UIModel for presenting tweets.
 */
data class PS2TweetUIModel(
    val user: String,
    val content: String,
    val tag: String,
    val creationTime: String,
    val imgUrl: String,
    val id: String,
    val sourceUrl: String,
)
