package com.cesarandres.ps2link.fragments.outfitpager.outfit

import com.cramsan.ps2link.appfrontend.BasePS2AndroidViewModel
import com.cramsan.ps2link.appfrontend.outfitpager.outfit.OutfitEventHandler
import com.cramsan.ps2link.appfrontend.outfitpager.outfit.OutfitViewModel
import com.cramsan.ps2link.appfrontend.outfitpager.outfit.OutfitViewModelInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Suppress("UndocumentedPublicClass")
@HiltViewModel
class OutfitAndroidViewModel @Inject constructor(
    viewModel: OutfitViewModel,
) : BasePS2AndroidViewModel<OutfitViewModel>(
    viewModel,
),
    OutfitEventHandler by viewModel,
    OutfitViewModelInterface by viewModel {

    override val logTag: String
        get() = "OutfitViewModel"
}
