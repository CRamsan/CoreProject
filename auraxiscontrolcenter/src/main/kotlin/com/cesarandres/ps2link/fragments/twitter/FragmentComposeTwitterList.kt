package com.cesarandres.ps2link.fragments.twitter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.fragment.app.viewModels
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BaseComposePS2Fragment
import com.cramsan.framework.core.requireAppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
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

    @Composable
    override fun CreateComposeContent() {
        val tweetList = viewModel.tweetList.observeAsState(emptyList())
        val twitterUsers = viewModel.twitterUsers.observeAsState(emptyMap())
        val isLoading = viewModel.isLoading.collectAsState()

        TweetListCompose(
            tweetItems = tweetList.value,
            users = twitterUsers.value,
            isLoading = isLoading.value,
            prettyTime = prettyTime,
            eventHandler = viewModel,
        )
    }

    override fun onResume() {
        super.onResume()
        requireAppCompatActivity().supportActionBar?.title = getString(R.string.title_twitter)
    }
}
