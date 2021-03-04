package com.cesarandres.ps2link.fragments.redditpager

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.fragment.app.viewModels
import com.cesarandres.ps2link.base.BaseComposePS2Fragment
import com.cramsan.ps2link.core.models.RedditPage
import dagger.hilt.android.AndroidEntryPoint
import org.ocpsoft.prettytime.PrettyTime
import javax.inject.Inject

/**
 * Fragment to display the list of locally stored profiles.
 */
@AndroidEntryPoint
class FragmentComposeReddit : BaseComposePS2Fragment<RedditViewModel>() {

    @Inject
    lateinit var prettyTime: PrettyTime

    override val logTag = "FragmentComposeProfile"
    override val viewModel: RedditViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val redditPage = arguments?.getSerializable(REDDIT_KEY) as RedditPage

        viewModel.setUp(redditPage)
    }

    @Composable
    override fun CreateComposeContent() {
        val redditContent = viewModel.redditContent.collectAsState(emptyList())
        val isLoading = viewModel.isLoading.collectAsState()
        RedditCompose(
            redditContent = redditContent.value,
            isLoading = isLoading.value,
            prettyTime = prettyTime,
            eventHandler = viewModel,
        )
    }

    companion object {

        private const val REDDIT_KEY = "namespace"

        fun instance(page: RedditPage): FragmentComposeReddit {
            val bundle = Bundle().apply {
                putSerializable(REDDIT_KEY, page)
            }
            return FragmentComposeReddit().apply {
                arguments = bundle
            }
        }
    }
}
