package com.cesarandres.ps2link.fragments.twitter

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.fragment.app.viewModels
import com.cesarandres.ps2link.base.BaseComposePS2Fragment
import com.cramsan.ps2link.appfrontend.twitter.TweetListCompose
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.collections.immutable.persistentListOf

/**
 * Fragment to display the list of tweets.
 */
@AndroidEntryPoint
class FragmentComposeTwitterList : BaseComposePS2Fragment<TwitterListAndroidViewModel>() {

    override val logTag = "FragmentComposeTwitterList"
    override val viewModel: TwitterListAndroidViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setUp()
    }

    @Composable
    override fun CreateComposeContent() {
        val tweetList = viewModel.tweetList.collectAsState(persistentListOf())
        val twitterUsers = viewModel.twitterUsers.collectAsState(emptyMap())
        val isLoading = viewModel.isLoading.collectAsState()
        val isError = viewModel.isError.collectAsState()

        TweetListCompose(
            tweetItems = tweetList.value,
            users = twitterUsers.value,
            isLoading = isLoading.value,
            isError = isError.value,
            eventHandler = viewModel,
        )
    }
}
