package com.cesarandres.ps2link.fragments.twitter

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cramsan.ps2link.core.models.PS2Tweet
import com.cramsan.ps2link.ui.FrameBottom
import com.cramsan.ps2link.ui.items.TweetItem

@Composable
fun TweetListCompose(
    tweetItems: List<PS2Tweet>,
) {
    FrameBottom(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            items(tweetItems) {
                TweetItem(
                    username = it.user
                )
            }
        }
    }
}

@Preview
@Composable
fun ServerListPreview() {
    TweetListCompose(
        tweetItems = emptyList(),
    )
}
