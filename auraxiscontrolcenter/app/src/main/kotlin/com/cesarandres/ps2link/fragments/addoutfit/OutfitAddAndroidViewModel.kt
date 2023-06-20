package com.cesarandres.ps2link.fragments.addoutfit

import com.cramsan.ps2link.appfrontend.BasePS2AndroidViewModel
import com.cramsan.ps2link.appfrontend.addoutfit.OutfitAddEventHandler
import com.cramsan.ps2link.appfrontend.addoutfit.OutfitAddViewModel
import com.cramsan.ps2link.appfrontend.addoutfit.OutfitAddViewModelInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Suppress("UndocumentedPublicClass")
@HiltViewModel
class OutfitAddAndroidViewModel @Inject constructor(
    override val viewModel: OutfitAddViewModel,
) : BasePS2AndroidViewModel<OutfitAddViewModel>(
    viewModel,
),
    OutfitAddEventHandler,
    OutfitAddViewModelInterface by viewModel
