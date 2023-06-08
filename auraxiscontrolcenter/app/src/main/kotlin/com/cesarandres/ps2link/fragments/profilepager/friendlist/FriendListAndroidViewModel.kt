package com.cesarandres.ps2link.fragments.profilepager.friendlist

import com.cramsan.ps2link.appfrontend.BasePS2AndroidViewModel
import com.cramsan.ps2link.appfrontend.profilepager.friendlist.FriendListEventHandler
import com.cramsan.ps2link.appfrontend.profilepager.friendlist.FriendListViewModel
import com.cramsan.ps2link.appfrontend.profilepager.friendlist.FriendListViewModelInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Suppress("UndocumentedPublicClass")
@HiltViewModel
class FriendListAndroidViewModel @Inject constructor(
    viewModel: FriendListViewModel,
) : BasePS2AndroidViewModel<FriendListViewModel>(
    viewModel,
),
    FriendListEventHandler by viewModel,
    FriendListViewModelInterface by viewModel {

    override val logTag: String
        get() = "FriendListViewModel"
}
