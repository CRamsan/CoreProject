package com.cesarandres.ps2link.fragments.profilepager.statlist

import com.cramsan.ps2link.appfrontend.BasePS2AndroidViewModel
import com.cramsan.ps2link.appfrontend.profilepager.statlist.StatListEventHandler
import com.cramsan.ps2link.appfrontend.profilepager.statlist.StatListViewModel
import com.cramsan.ps2link.appfrontend.profilepager.statlist.StatListViewModelInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Suppress("UndocumentedPublicClass")
@HiltViewModel
class StatListAndroidViewModel @Inject constructor(
    viewModel: StatListViewModel,
) : BasePS2AndroidViewModel<StatListViewModel>(
    viewModel,
),
    StatListEventHandler by viewModel,
    StatListViewModelInterface by viewModel {

    override val logTag: String
        get() = "ProfileViewModel"
}
