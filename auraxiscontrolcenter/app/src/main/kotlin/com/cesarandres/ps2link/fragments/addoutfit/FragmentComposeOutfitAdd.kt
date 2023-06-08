package com.cesarandres.ps2link.fragments.addoutfit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.fragment.app.viewModels
import com.cesarandres.ps2link.base.BaseComposePS2Fragment
import com.cramsan.ps2link.appfrontend.addoutfit.OutfitAddCompose
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment to display the list of locally stored outfits.
 */
@AndroidEntryPoint
class FragmentComposeOutfitAdd : BaseComposePS2Fragment<OutfitAddAndroidViewModel>() {

    override val logTag = "FragmentComposeOutfitAdd"
    override val viewModel: OutfitAddAndroidViewModel by viewModels()

    @Composable
    override fun CreateComposeContent() {
        val tagSearchQueryState = viewModel.tagSearchQuery.collectAsState()
        val nameSearchQueryState = viewModel.nameSearchQuery.collectAsState()
        val profileList = viewModel.outfitList.collectAsState()
        val isLoading = viewModel.isLoading.collectAsState()
        val isError = viewModel.isError.collectAsState()
        OutfitAddCompose(
            tagSearchField = tagSearchQueryState.value,
            nameSearchField = nameSearchQueryState.value,
            outfitItems = profileList.value,
            isLoading = isLoading.value,
            isError = isError.value,
            eventHandler = viewModel,
        )
    }
}
