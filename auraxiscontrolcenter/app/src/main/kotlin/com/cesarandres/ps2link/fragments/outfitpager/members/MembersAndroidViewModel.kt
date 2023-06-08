package com.cesarandres.ps2link.fragments.outfitpager.members

import com.cramsan.ps2link.appfrontend.BasePS2AndroidViewModel
import com.cramsan.ps2link.appfrontend.outfitpager.members.MemberListEventHandler
import com.cramsan.ps2link.appfrontend.outfitpager.members.MembersViewModel
import com.cramsan.ps2link.appfrontend.outfitpager.members.MembersViewModelInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Suppress("UndocumentedPublicClass")
@HiltViewModel
class MembersAndroidViewModel @Inject constructor(
    viewModel: MembersViewModel,
) : BasePS2AndroidViewModel<MembersViewModel>(
    viewModel,
),
    MemberListEventHandler by viewModel,
    MembersViewModelInterface by viewModel {

    override val logTag: String
        get() = "MembersAndroidViewModel"
}
