package com.cesarandres.ps2link.fragments.addprofile

import com.cramsan.ps2link.appfrontend.BasePS2AndroidViewModel
import com.cramsan.ps2link.appfrontend.addprofile.ProfileAddEventHandler
import com.cramsan.ps2link.appfrontend.addprofile.ProfileAddViewModel
import com.cramsan.ps2link.appfrontend.addprofile.ProfileAddViewModelInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Suppress("UndocumentedPublicClass")
@HiltViewModel
class ProfileAddAndroidViewModel @Inject constructor(
    viewModel: ProfileAddViewModel,
) : BasePS2AndroidViewModel<ProfileAddViewModel>(
    viewModel,
),
    ProfileAddEventHandler by viewModel,
    ProfileAddViewModelInterface by viewModel
