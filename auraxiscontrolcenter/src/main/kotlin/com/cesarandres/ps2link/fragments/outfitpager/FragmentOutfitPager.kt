package com.cesarandres.ps2link.fragments.outfitpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BasePS2Fragment
import com.cesarandres.ps2link.databinding.FragmentOutfitPagerBinding
import com.cesarandres.ps2link.fragments.outfitpager.online.FragmentComposeOnlineMembers
import com.cesarandres.ps2link.fragments.outfitpager.outfit.FragmentComposeOutfit
import com.cramsan.ps2link.core.models.Namespace
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

/**
 * This fragment holds a view pager for all the profile related fragments
 */
@AndroidEntryPoint
class FragmentOutfitPager : BasePS2Fragment<OutfitPagerViewModel, FragmentOutfitPagerBinding>() {

    override val viewModel: OutfitPagerViewModel by viewModels()
    override val logTag = "FragmentOutfitPager"
    override val contentViewLayout = R.layout.fragment_outfit_pager
    val args: FragmentOutfitPagerArgs by navArgs()

    private lateinit var viewPager: ViewPager2
    private lateinit var outfitId: String
    private lateinit var namespace: Namespace

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        setHasOptionsMenu(true)

        viewPager = dataBinding.outfitPager

        // The pager adapter, which provides the pages to the view pager widget.
        val pagerAdapter = ScreenSlidePagerAdapter(requireActivity())
        viewPager.adapter = pagerAdapter

        val tabLayout = dataBinding.tabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            // TODO: Replace with string resource
            tab.text = OutfitPage.values()[position].name
        }.attach()

        outfitId = args.outfitId
        namespace = args.namespace

        viewModel.setUp(outfitId, namespace)
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.outfit_menu, menu)
        return
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add -> {
                viewModel.addOutfit()
            }
            R.id.action_remove -> {
                viewModel.removeOutfit()
            }
            R.id.action_star -> {
                viewModel.pinOutfit()
            }
            R.id.action_unstar -> {
                viewModel.unpinOutfit()
            }
            R.id.action_update -> {
            }
        }
        return true
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = OutfitPage.values().size

        override fun createFragment(position: Int): Fragment {
            return when (OutfitPage.values()[position]) {
                OutfitPage.OUTFIT -> FragmentComposeOutfit.instance(outfitId, namespace)
                OutfitPage.ONLINE -> FragmentComposeOnlineMembers.instance(outfitId, namespace)
                OutfitPage.MEMBERS -> FragmentComposeOutfit.instance(outfitId, namespace)
            }
        }
    }

    private enum class OutfitPage {
        OUTFIT,
        ONLINE,
        MEMBERS,
    }
}
