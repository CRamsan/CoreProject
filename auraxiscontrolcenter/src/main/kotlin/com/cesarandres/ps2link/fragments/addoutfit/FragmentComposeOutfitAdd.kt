package com.cesarandres.ps2link.fragments.addoutfit

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
import com.cesarandres.ps2link.fragments.OpenOutfit
import com.cramsan.framework.core.BaseEvent
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment to display the list of locally stored outfits.
 */
@AndroidEntryPoint
class FragmentComposeOutfitAdd : BaseComposePS2Fragment<OutfitAddViewModel>() {

    override val logTag = "FragmentComposeOutfitAdd"
    override val viewModel: OutfitAddViewModel by viewModels()

    @Composable
    override fun CreateComposeContent() {
        val tagSearchQueryState = viewModel.tagSearchQuery.collectAsState()
        val nameSearchQueryState = viewModel.nameSearchQuery.collectAsState()
        val profileList = viewModel.outfitList.collectAsState()
        val isLoading = viewModel.isLoading.collectAsState()
        OutfitAddCompose(
            tagSearchField = tagSearchQueryState.value,
            nameSearchField = nameSearchQueryState.value,
            outfitItems = profileList.value,
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
        requireActivity().title = getString(R.string.title_outfits)
        return view
    }

    override fun onViewModelEvent(event: BaseEvent) {
        super.onViewModelEvent(event)
        when (event) {
            is OpenOutfit -> {
                val action = FragmentComposeOutfitAddDirections.actionFragmentAddOutfitToFragmentOutfitPager(event.outfitId, event.namespace)
                findNavController().navigate(action)
            }
        }
    }
}
