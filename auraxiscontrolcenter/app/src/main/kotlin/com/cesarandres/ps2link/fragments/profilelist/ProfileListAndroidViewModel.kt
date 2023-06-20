package com.cesarandres.ps2link.fragments.profilelist

import com.cramsan.ps2link.appfrontend.BasePS2AndroidViewModel
import com.cramsan.ps2link.appfrontend.profilelist.ProfileListEventHandler
import com.cramsan.ps2link.appfrontend.profilelist.ProfileListViewModel
import com.cramsan.ps2link.appfrontend.profilelist.ProfileListViewModelInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Suppress("UndocumentedPublicClass")
@HiltViewModel
class ProfileListAndroidViewModel @Inject constructor(
    viewModel: ProfileListViewModel,
) : BasePS2AndroidViewModel<ProfileListViewModel>(
    viewModel
),
    ProfileListEventHandler by viewModel,
    ProfileListViewModelInterface by viewModel
