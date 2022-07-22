package com.cesarandres.ps2link.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.databinding.FragmentPagerBinding
import com.cramsan.framework.core.BaseViewModel
import com.google.android.material.tabs.TabLayoutMediator

/**
 * This fragment holds a generic view pager
 */
abstract class BasePS2FragmentPager<VM : BaseViewModel> : BasePS2Fragment<VM, FragmentPagerBinding>() {

    override val logTag = "BasePS2FragmentPager"
    override val contentViewLayout = R.layout.fragment_pager

    private lateinit var viewPager: ViewPager2

    // TODO: Migrate to the new MenuProvider API
    // https://developer.android.com/jetpack/androidx/releases/activity#1.4.0-alpha01
    @Suppress("DEPRECATION")
    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        setHasOptionsMenu(true)

        viewPager = dataBinding.fragmentPager

        // The pager adapter, which provides the pages to the view pager widget.
        val pagerAdapter = ScreenSlidePagerAdapter(requireActivity())
        viewPager.adapter = pagerAdapter
        viewPager.isSaveEnabled = false

        val tabLayout = dataBinding.tabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = pageTitle(position)
        }.attach()

        return view
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount() = this@BasePS2FragmentPager.itemCount()

        override fun createFragment(position: Int) = this@BasePS2FragmentPager.createFragment(position)
    }

    abstract fun itemCount(): Int

    abstract fun pageTitle(position: Int): CharSequence

    abstract fun createFragment(position: Int): Fragment
}
