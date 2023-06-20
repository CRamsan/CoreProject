package com.cesarandres.ps2link.fragments.profilepager.killlist

import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import com.cramsan.ps2link.appfrontend.BasePS2AndroidViewModel
import com.cramsan.ps2link.appfrontend.LanguageProvider
import com.cramsan.ps2link.appfrontend.profilepager.killlist.KillListEventHandler
import com.cramsan.ps2link.appfrontend.profilepager.killlist.KillListViewModel
import com.cramsan.ps2link.appfrontend.profilepager.killlist.KillListViewModelInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@Suppress("UndocumentedPublicClass")
@HiltViewModel
class KillListAndroidViewModel @Inject constructor(
    override val viewModel: KillListViewModel,
) : BasePS2AndroidViewModel<KillListViewModel>(
    viewModel,
),
    KillListEventHandler,
    KillListViewModelInterface by viewModel {

    override val logTag: String
        get() = "KillListViewModel"
    override val isError: StateFlow<Boolean>
        get() = viewModel.isError
    override val isLoading: StateFlow<Boolean>
        get() = viewModel.isLoading
    override val languageProvider: LanguageProvider
        get() = viewModel.languageProvider

    override fun loadingCompleted() {
        viewModel.loadingCompleted()
    }

    override fun loadingCompletedWithError() {
        viewModel.loadingCompletedWithError()
    }

    override fun loadingStarted() {
        viewModel.loadingStarted()
    }

    override val pS2LinkRepository: PS2LinkRepository
        get() = viewModel.pS2LinkRepository
    override val ps2Settings: PS2Settings
        get() = viewModel.ps2Settings
}
