package com.cesarandres.ps2link.fragments.serverlist

import com.cramsan.ps2link.appfrontend.BasePS2AndroidViewModel
import com.cramsan.ps2link.appfrontend.serverlist.ServerListEventHandler
import com.cramsan.ps2link.appfrontend.serverlist.ServerListViewModel
import com.cramsan.ps2link.appfrontend.serverlist.ServerListViewModelInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Suppress("UndocumentedPublicClass")
@HiltViewModel
class ServerListAndroidViewModel @Inject constructor(
    viewModel: ServerListViewModel,
) : BasePS2AndroidViewModel<ServerListViewModel>(
    viewModel,
),
    ServerListEventHandler by viewModel,
    ServerListViewModelInterface by viewModel {

    override val logTag: String
        get() = "ServerListViewModel"
}
