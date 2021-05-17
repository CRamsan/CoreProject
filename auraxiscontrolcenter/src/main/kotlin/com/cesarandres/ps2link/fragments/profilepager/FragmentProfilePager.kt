package com.cesarandres.ps2link.fragments.profilepager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BasePS2FragmentPager
import com.cesarandres.ps2link.fragments.profilepager.friendlist.FragmentComposeFriendList
import com.cesarandres.ps2link.fragments.profilepager.killlist.FragmentComposeKillList
import com.cesarandres.ps2link.fragments.profilepager.profile.FragmentComposeProfile
import com.cesarandres.ps2link.fragments.profilepager.statlist.FragmentComposeStatList
import com.cesarandres.ps2link.fragments.profilepager.weaponlist.FragmentComposeWeaponList
import com.cramsan.framework.core.requireAppCompatActivity
import com.cramsan.ps2link.core.models.Namespace
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * This fragment holds a view pager for all the profile related fragments
 */
@AndroidEntryPoint
class FragmentProfilePager : BasePS2FragmentPager<ProfilePagerViewModel>() {

    override val viewModel: ProfilePagerViewModel by viewModels()
    override val logTag = "FragmentProfilePager"
    private val args: FragmentProfilePagerArgs by navArgs()

    private lateinit var profileId: String
    private lateinit var namespace: Namespace

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        setHasOptionsMenu(true)

        profileId = args.characterId
        namespace = args.namespace

        viewModel.setUp(profileId, namespace)
        viewModel.title.asLiveData().observe(viewLifecycleOwner) {
            val title = it ?: getString(R.string.text_unknown)
            requireAppCompatActivity().supportActionBar?.title = title
        }
        viewModel.displayAddCharacter.asLiveData().observe(viewLifecycleOwner) {
            requireActivity().invalidateOptionsMenu()
        }
        viewModel.displayPreferProfile.asLiveData().observe(viewLifecycleOwner) {
            requireActivity().invalidateOptionsMenu()
        }
        viewModel.displayRemoveCharacter.asLiveData().observe(viewLifecycleOwner) {
            requireActivity().invalidateOptionsMenu()
        }
        viewModel.displayUnpreferProfile.asLiveData().observe(viewLifecycleOwner) {
            requireActivity().invalidateOptionsMenu()
        }

        return view
    }

    override fun itemCount() = ProfilePage.values().size

    override fun pageTitle(position: Int) = ProfilePage.values()[position].name

    override fun createFragment(position: Int): Fragment {
        return when (ProfilePage.values()[position]) {
            ProfilePage.PROFILE -> FragmentComposeProfile.instance(profileId, namespace)
            ProfilePage.FRIENDS -> FragmentComposeFriendList.instance(profileId, namespace)
            ProfilePage.STATS -> FragmentComposeStatList.instance(profileId, namespace)
            ProfilePage.KILLBOARD -> FragmentComposeKillList.instance(profileId, namespace)
            ProfilePage.WEAPONS -> FragmentComposeWeaponList.instance(profileId, namespace)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.profile_menu, menu)
        val addButton = menu.findItem(R.id.action_add)
        val removeButton = menu.findItem(R.id.action_remove)
        val starButton = menu.findItem(R.id.action_star)
        val unstarButton = menu.findItem(R.id.action_unstar)
        addButton.isVisible = viewModel.displayAddCharacter.value
        removeButton.isVisible = viewModel.displayRemoveCharacter.value
        starButton.isVisible = viewModel.displayPreferProfile.value
        unstarButton.isVisible = viewModel.displayUnpreferProfile.value
        return
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        lifecycleScope.launch {
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
            }
        }
        return true
    }

    private enum class ProfilePage {
        PROFILE,
        FRIENDS,
        STATS,
        KILLBOARD,
        WEAPONS,
    }
}
