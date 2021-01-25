package com.cesarandres.ps2link.fragments.profilepager

import android.os.Bundle
import android.view.LayoutInflater
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
import com.cesarandres.ps2link.databinding.FragmentProfilePagerBinding
import com.cesarandres.ps2link.fragments.profilepager.friendlist.FragmentComposeFriendList
import com.cesarandres.ps2link.fragments.profilepager.profile.FragmentComposeProfile
import com.cramsan.framework.core.NoopViewModel
import com.cramsan.ps2link.appcore.dbg.Namespace
import dagger.hilt.android.AndroidEntryPoint

/**
 * This fragment holds a view pager for all the profile related fragments
 */
@AndroidEntryPoint
class FragmentProfilePager : BasePS2Fragment<NoopViewModel, FragmentProfilePagerBinding>() {

    override val viewModel: NoopViewModel by viewModels()
    override val logTag = "FragmentProfilePager"
    override val contentViewLayout = R.layout.fragment_profile_pager
    val args: FragmentProfilePagerArgs by navArgs()

    private lateinit var viewPager: ViewPager2
    private lateinit var profileId: String
    private lateinit var namespace: Namespace

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        viewPager = dataBinding.profilePager
        profileId = args.characterId
        namespace = args.namespace

        // The pager adapter, which provides the pages to the view pager widget.
        val pagerAdapter = ScreenSlidePagerAdapter(requireActivity())
        viewPager.adapter = pagerAdapter
        return view
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = ProfilePage.values().size

        override fun createFragment(position: Int): Fragment {
            return when (ProfilePage.values()[position]) {
                ProfilePage.PROFILE -> FragmentComposeProfile.instance(profileId, namespace)
                ProfilePage.FRIENDS -> FragmentComposeFriendList.instance(profileId, namespace)
                /*
                ProfilePage.STATS -> FragmentStatList()
                ProfilePage.KILLBOARD -> FragmentKillList()
                ProfilePage.WEAPONS -> FragmentWeaponList()
                 */
            }
        }
    }

    private enum class ProfilePage {
        PROFILE,
        FRIENDS,
        /*
        STATS,
        KILLBOARD,
        WEAPONS,
         */
    }
}
