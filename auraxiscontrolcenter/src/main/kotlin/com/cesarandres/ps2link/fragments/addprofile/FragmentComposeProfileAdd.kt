package com.cesarandres.ps2link.fragments.addprofile

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
import com.cesarandres.ps2link.fragments.OpenProfile
import com.cramsan.framework.core.BaseEvent
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment to display the list of locally stored profiles.
 */
@AndroidEntryPoint
class FragmentComposeProfileAdd : BaseComposePS2Fragment<ProfileAddViewModel>() {

    override val logTag = "FragmentComposeMainMenu"
    override val viewModel: ProfileAddViewModel by viewModels()

    @Composable
    override fun CreateComposeContent() {
        val searchQueryState = viewModel.searchQuery.collectAsState()
        val profileList = viewModel.profileList.collectAsState()
        val isLoading = viewModel.isLoading.collectAsState()
        ProfileAddCompose(
            searchField = searchQueryState.value,
            profileItems = profileList.value,
            isLoading = isLoading.value,
            eventHandler = viewModel
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        requireActivity().title = getString(R.string.title_profiles)
        return view
    }

    override fun onViewModelEvent(event: BaseEvent) {
        super.onViewModelEvent(event)
        when (event) {
            is OpenProfile -> {
                val action = FragmentComposeProfileAddDirections.actionFragmentAddProfileToFragmentProfile(event.characterId)
                findNavController().navigate(action)
            }
        }
    }
}
