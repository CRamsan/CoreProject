package com.cesarandres.ps2link.fragments.outfitlist

import com.cramsan.ps2link.appfrontend.BasePS2AndroidViewModel
import com.cramsan.ps2link.appfrontend.outfitlist.OutfitListEventHandler
import com.cramsan.ps2link.appfrontend.outfitlist.OutfitListViewModel
import com.cramsan.ps2link.appfrontend.outfitlist.OutfitListViewModelInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Suppress("UndocumentedPublicClass")
@HiltViewModel
class OutfitListAndroidViewModel @Inject constructor(
    viewModel: OutfitListViewModel,
) : BasePS2AndroidViewModel<OutfitListViewModel>(
    viewModel,
),
    OutfitListEventHandler by viewModel,
    OutfitListViewModelInterface by viewModel
