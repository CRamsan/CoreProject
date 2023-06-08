package com.cesarandres.ps2link.fragments.profilepager.killlist

import com.cramsan.ps2link.appfrontend.BasePS2AndroidViewModel
import com.cramsan.ps2link.appfrontend.profilepager.killlist.KillListEventHandler
import com.cramsan.ps2link.appfrontend.profilepager.killlist.KillListViewModel
import com.cramsan.ps2link.appfrontend.profilepager.killlist.KillListViewModelInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Suppress("UndocumentedPublicClass")
@HiltViewModel
class KillListAndroidViewModel @Inject constructor(
    viewModel: KillListViewModel,
) : BasePS2AndroidViewModel<KillListViewModel>(
    viewModel,
),
    KillListEventHandler by viewModel,
    KillListViewModelInterface by viewModel {

    override val logTag: String
        get() = "KillListViewModel"
}
