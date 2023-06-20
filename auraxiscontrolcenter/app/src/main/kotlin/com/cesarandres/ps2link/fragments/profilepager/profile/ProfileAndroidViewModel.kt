package com.cesarandres.ps2link.fragments.profilepager.profile

import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import com.cramsan.ps2link.appfrontend.BasePS2AndroidViewModel
import com.cramsan.ps2link.appfrontend.LanguageProvider
import com.cramsan.ps2link.appfrontend.profilepager.profile.ProfileEventHandler
import com.cramsan.ps2link.appfrontend.profilepager.profile.ProfileViewModel
import com.cramsan.ps2link.appfrontend.profilepager.profile.ProfileViewModelInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@Suppress("UndocumentedPublicClass")
@HiltViewModel
class ProfileAndroidViewModel @Inject constructor(
    override val viewModel: ProfileViewModel,
) : BasePS2AndroidViewModel<ProfileViewModel>(
    viewModel,
),
    ProfileEventHandler,
    ProfileViewModelInterface by viewModel {
    override val logTag: String = "ProfileViewModel"
    override val isError: StateFlow<Boolean> = viewModel.isError
    override val isLoading: StateFlow<Boolean> = viewModel.isLoading
    override val languageProvider: LanguageProvider = viewModel.languageProvider

    override fun loadingCompleted() {
        viewModel.loadingCompleted()
    }

    override fun loadingCompletedWithError() {
        viewModel.loadingCompletedWithError()
    }

    override fun loadingStarted() {
        viewModel.loadingStarted()
    }

    override val pS2LinkRepository: PS2LinkRepository = viewModel.pS2LinkRepository
    override val ps2Settings: PS2Settings = viewModel.ps2Settings
}
