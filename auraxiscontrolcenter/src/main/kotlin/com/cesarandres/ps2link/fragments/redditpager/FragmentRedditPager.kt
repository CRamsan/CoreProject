package com.cesarandres.ps2link.fragments.redditpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BasePS2Fragment
import com.cesarandres.ps2link.databinding.FragmentRedditPagerBinding
import com.cramsan.framework.core.NoopViewModel
import com.cramsan.ps2link.core.models.RedditPage
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

/**
 * This fragment holds a view pager for a fragment for each subredit
 */
@AndroidEntryPoint
class FragmentRedditPager : BasePS2Fragment<NoopViewModel, FragmentRedditPagerBinding>() {

    override val viewModel: NoopViewModel by viewModels()
    override val logTag = "FragmentRedditPager"
    override val contentViewLayout = R.layout.fragment_reddit_pager

    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        setHasOptionsMenu(true)

        viewPager = dataBinding.redditPager

        // The pager adapter, which provides the pages to the view pager widget.
        val pagerAdapter = ScreenSlidePagerAdapter(requireActivity())
        viewPager.adapter = pagerAdapter

        val tabLayout = dataBinding.tabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = RedditPage.values()[position].path
        }.attach()

        return view
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = RedditPage.values().size

        override fun createFragment(position: Int): Fragment {
            return FragmentComposeReddit.instance(RedditPage.values()[position])
        }
    }
}
