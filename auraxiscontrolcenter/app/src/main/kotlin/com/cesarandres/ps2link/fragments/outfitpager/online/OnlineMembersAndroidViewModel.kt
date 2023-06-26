package com.cesarandres.ps2link.fragments.outfitpager.online

import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import com.cramsan.ps2link.appfrontend.BasePS2AndroidViewModel
import com.cramsan.ps2link.appfrontend.LanguageProvider
import com.cramsan.ps2link.appfrontend.outfitpager.online.OnlineMemberEventHandler
import com.cramsan.ps2link.appfrontend.outfitpager.online.OnlineMembersViewModel
import com.cramsan.ps2link.appfrontend.outfitpager.online.OnlineMembersViewModelInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@Suppress("UndocumentedPublicClass")
@HiltViewModel
class OnlineMembersAndroidViewModel @Inject constructor(
    override val viewModel: OnlineMembersViewModel,
) : BasePS2AndroidViewModel<OnlineMembersViewModel>(
    viewModel,
),
    OnlineMemberEventHandler,
    OnlineMembersViewModelInterface by viewModel {

    override val logTag: String
        get() = "OnlineMembersViewModel"
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