package com.cesarandres.ps2link.fragments.redditpager

import androidx.fragment.app.viewModels
import com.cesarandres.ps2link.base.BasePS2FragmentPager
import com.cramsan.framework.core.NoopViewModel
import com.cramsan.ps2link.core.models.RedditPage
import dagger.hilt.android.AndroidEntryPoint

/**
 * This fragment holds a view pager for a fragment for each subredit
 */
@AndroidEntryPoint
class FragmentRedditPager : BasePS2FragmentPager<NoopViewModel>() {

    override val viewModel: NoopViewModel by viewModels()
    override val logTag = "FragmentRedditPager"

    override fun itemCount() = RedditPage.values().size

    override fun pageTitle(position: Int) = RedditPage.values()[position].path

    override fun createFragment(position: Int) =
        FragmentComposeReddit.instance(RedditPage.values()[position])
}
