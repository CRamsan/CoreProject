package com.cesarandres.ps2link.fragments.twitter

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.fragment.app.viewModels
import com.cesarandres.ps2link.base.BaseComposePS2Fragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.collections.immutable.persistentListOf
import org.ocpsoft.prettytime.PrettyTime
import javax.inject.Inject

/**
 * Fragment to display the list of tweets.
 */
@AndroidEntryPoint
class FragmentComposeTwitterList : BaseComposePS2Fragment<TwitterListViewModel>() {

    override val logTag = "FragmentComposeTwitterList"
    override val viewModel: TwitterListViewModel by viewModels()

    @Inject
    lateinit var prettyTime: PrettyTime

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setUp()
    }

    @Composable
    override fun CreateComposeContent() {
        val tweetList = viewModel.tweetList.observeAsState(persistentListOf())
        val twitterUsers = viewModel.twitterUsers.observeAsState(emptyMap())
        val isLoading = viewModel.isLoading.collectAsState()
        val isError = viewModel.isError.collectAsState()

        TweetListCompose(
            tweetItems = tweetList.value,
            users = twitterUsers.value,
            isLoading = isLoading.value,
            isError = isError.value,
            prettyTime = prettyTime,
            eventHandler = viewModel,
        )
    }
}
