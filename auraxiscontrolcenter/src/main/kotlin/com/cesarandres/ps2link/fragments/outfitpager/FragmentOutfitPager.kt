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
import com.cesarandres.ps2link.fragments.profilepager.ProfilePagerViewModel
import com.cesarandres.ps2link.fragments.profilepager.friendlist.FragmentComposeFriendList
import com.cesarandres.ps2link.fragments.profilepager.profile.FragmentComposeProfile
import com.cesarandres.ps2link.fragments.profilepager.statlist.FragmentComposeStatList
import com.cramsan.ps2link.core.models.Namespace
import dagger.hilt.android.AndroidEntryPoint

/**
 * This fragment holds a view pager for all the profile related fragments
 */
@AndroidEntryPoint
class FragmentOutfitPager : BasePS2Fragment<ProfilePagerViewModel, FragmentOutfitPagerBinding>() {

    override val viewModel: ProfilePagerViewModel by viewModels()
    override val logTag = "FragmentProfilePager"
    override val contentViewLayout = R.layout.fragment_profile_pager
    val args: FragmentOutfitPagerArgs by navArgs()

    private lateinit var viewPager: ViewPager2
    private lateinit var profileId: String
    private lateinit var namespace: Namespace

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        setHasOptionsMenu(true)

        viewPager = dataBinding.outfitPager
        profileId = args.characterId
        namespace = args.namespace

        // The pager adapter, which provides the pages to the view pager widget.
        val pagerAdapter = ScreenSlidePagerAdapter(requireActivity())
        viewPager.adapter = pagerAdapter
        viewModel.setUp(profileId, namespace)
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
                viewModel.addCharacter()
            }
            R.id.action_remove -> {
                viewModel.removeCharacter()
            }
            R.id.action_star -> {
                viewModel.pinCharacter()
            }
            R.id.action_unstar -> {
                viewModel.unpinCharacter()
            }
            R.id.action_update -> {
            }
        }
        return true
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = ProfilePage.values().size

        override fun createFragment(position: Int): Fragment {
            return when (ProfilePage.values()[position]) {
                ProfilePage.OUTFIT -> FragmentComposeProfile.instance(profileId, namespace)
                ProfilePage.ONLINE -> FragmentComposeFriendList.instance(profileId, namespace)
                ProfilePage.MEMBERS -> FragmentComposeStatList.instance(profileId, namespace)
            }
        }
    }

    private enum class ProfilePage {
        OUTFIT,
        ONLINE,
        MEMBERS,
    }
}
