package com.cesarandres.ps2link.fragments.mainmenu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BaseComposePS2Fragment
import com.cesarandres.ps2link.fragments.OpenAbout
import com.cesarandres.ps2link.fragments.OpenOutfit
import com.cesarandres.ps2link.fragments.OpenOutfitList
import com.cesarandres.ps2link.fragments.OpenProfile
import com.cesarandres.ps2link.fragments.OpenProfileList
import com.cesarandres.ps2link.fragments.OpenReddit
import com.cesarandres.ps2link.fragments.OpenServerList
import com.cesarandres.ps2link.fragments.OpenTwitter
import com.cramsan.framework.core.BaseEvent
import dagger.hilt.android.AndroidEntryPoint

/**
 * This fragment is very static, it has all the buttons for most of the main
 * fragments. It will also display the Preferred Character and Preferred Outfit
 * buttons if those have been set.
 */
@AndroidEntryPoint
class FragmentComposeMainMenu : BaseComposePS2Fragment<MainMenuViewModel>() {

    override val logTag = "FragmentComposeMainMenu"
    override val viewModel: MainMenuViewModel by viewModels()

    @Composable
    override fun CreateComposeContent() {
        val preferredProfile = viewModel.preferredProfile.collectAsState(null)
        val preferredOutfit = viewModel.preferredOutfit.collectAsState(null)
        MainMenuCompose(
            preferredProfile = preferredProfile.value,
            preferredOutfit = preferredOutfit.value,
            eventHandler = viewModel,
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        requireActivity().title = getString(R.string.app_name_capital)
        return view
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateUI()
    }

    override fun onViewModelEvent(event: BaseEvent) {
        super.onViewModelEvent(event)
        when (event) {
            is OpenProfile -> {
                val action = FragmentComposeMainMenuDirections.actionMainMenuFragmentToFragmentProfile(event.characterId, event.namespace)
                findNavController().navigate(action)
            }
            is OpenOutfit -> {
                val action = FragmentComposeMainMenuDirections.actionMainMenuFragmentToFragmentOutfitPager(event.outfitId, event.namespace)
                findNavController().navigate(action)
            }
            is OpenProfileList -> {
                val action = FragmentComposeMainMenuDirections.actionMainMenuFragmentToFragmentProfileList()
                findNavController().navigate(action)
            }
            is OpenOutfitList -> {
                val action = FragmentComposeMainMenuDirections.actionMainMenuFragmentToFragmentOutfitList()
                findNavController().navigate(action)
            }
            is OpenServerList -> {
                val action = FragmentComposeMainMenuDirections.actionMainMenuFragmentToFragmentServerList()
                findNavController().navigate(action)
            }
            is OpenTwitter -> {
                val action = FragmentComposeMainMenuDirections.actionMainMenuFragmentToFragmentTwitter()
                findNavController().navigate(action)
            }
            is OpenReddit -> {
                val action = FragmentComposeMainMenuDirections.actionMainMenuFragmentToFragmentReddit()
                findNavController().navigate(action)
            }
            is OpenAbout -> {
                val action = FragmentComposeMainMenuDirections.actionMainMenuFragmentToFragmentAbout()
                findNavController().navigate(action)
            }
        }
    }
}
