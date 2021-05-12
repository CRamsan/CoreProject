package com.cesarandres.ps2link.fragments.addprofile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.fragment.app.viewModels
import com.cesarandres.ps2link.base.BaseComposePS2Fragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment to display the list of locally stored profiles.
 */
@AndroidEntryPoint
class FragmentComposeProfileAdd : BaseComposePS2Fragment<ProfileAddViewModel>() {

    override val logTag = "FragmentComposeProfileAdd"
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
}
