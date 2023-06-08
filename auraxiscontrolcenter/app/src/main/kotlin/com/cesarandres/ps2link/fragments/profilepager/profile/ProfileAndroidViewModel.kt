package com.cesarandres.ps2link.fragments.profilepager.profile

import com.cramsan.ps2link.appfrontend.BasePS2AndroidViewModel
import com.cramsan.ps2link.appfrontend.profilepager.profile.ProfileEventHandler
import com.cramsan.ps2link.appfrontend.profilepager.profile.ProfileViewModel
import com.cramsan.ps2link.appfrontend.profilepager.profile.ProfileViewModelInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Suppress("UndocumentedPublicClass")
@HiltViewModel
class ProfileAndroidViewModel @Inject constructor(
    viewModel: ProfileViewModel,
) : BasePS2AndroidViewModel<ProfileViewModel>(
    viewModel,
),
    ProfileEventHandler by viewModel,
    ProfileViewModelInterface by viewModel {

    override val logTag: String
        get() = "ProfileViewModel"
}
