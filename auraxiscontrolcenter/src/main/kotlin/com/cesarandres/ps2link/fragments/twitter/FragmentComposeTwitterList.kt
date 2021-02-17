package com.cesarandres.ps2link.fragments.twitter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.fragment.app.viewModels
import com.cesarandres.ps2link.base.BaseComposePS2Fragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment to display the list of tweets.
 */
@AndroidEntryPoint
class FragmentComposeTwitterList : BaseComposePS2Fragment<TwitterListViewModel>() {

    override val logTag = "FragmentComposeTwitterList"
    override val viewModel: TwitterListViewModel by viewModels()

    @Composable
    override fun CreateComposeContent() {
        val tweetList = viewModel.tweetList.observeAsState(emptyList())
        TweetListCompose(
            tweetItems = tweetList.value,
        )
    }
}
