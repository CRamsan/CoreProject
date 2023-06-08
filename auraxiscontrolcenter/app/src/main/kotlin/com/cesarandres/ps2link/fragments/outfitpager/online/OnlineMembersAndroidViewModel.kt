package com.cesarandres.ps2link.fragments.outfitpager.online

import com.cramsan.ps2link.appfrontend.BasePS2AndroidViewModel
import com.cramsan.ps2link.appfrontend.outfitpager.online.OnlineMemberEventHandler
import com.cramsan.ps2link.appfrontend.outfitpager.online.OnlineMembersViewModel
import com.cramsan.ps2link.appfrontend.outfitpager.online.OnlineMembersViewModelInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Suppress("UndocumentedPublicClass")
@HiltViewModel
class OnlineMembersAndroidViewModel @Inject constructor(
    viewModel: OnlineMembersViewModel,
) : BasePS2AndroidViewModel<OnlineMembersViewModel>(
    viewModel,
),
    OnlineMemberEventHandler by viewModel,
    OnlineMembersViewModelInterface by viewModel {

    override val logTag: String
        get() = "OnlineMembersViewModel"
}
