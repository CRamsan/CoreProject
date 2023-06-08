package com.cesarandres.ps2link.fragments.mainmenu

import com.cramsan.ps2link.appfrontend.BasePS2AndroidViewModel
import com.cramsan.ps2link.appfrontend.mainmenu.MainMenuEventHandler
import com.cramsan.ps2link.appfrontend.mainmenu.MainMenuViewModel
import com.cramsan.ps2link.appfrontend.mainmenu.MainMenuViewModelInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Suppress("UndocumentedPublicClass")
@HiltViewModel
class MainMenuAndroidViewModel @Inject constructor(
    viewModel: MainMenuViewModel,
) : BasePS2AndroidViewModel<MainMenuViewModel>(
    viewModel,
),
    MainMenuEventHandler by viewModel,
    MainMenuViewModelInterface by viewModel {

    override val logTag: String
        get() = "MainMenuAndroidViewModel"
}
