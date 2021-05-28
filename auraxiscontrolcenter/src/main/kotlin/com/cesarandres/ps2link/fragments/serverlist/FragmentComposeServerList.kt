package com.cesarandres.ps2link.fragments.serverlist

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.fragment.app.viewModels
import com.cesarandres.ps2link.base.BaseComposePS2Fragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment to display the list of locally stored profiles.
 */
@AndroidEntryPoint
class FragmentComposeServerList : BaseComposePS2Fragment<ServerListViewModel>() {

    override val logTag = "FragmentComposeServerList"
    override val viewModel: ServerListViewModel by viewModels()

    @Composable
    override fun CreateComposeContent() {
        val serverList = viewModel.serverList.observeAsState(emptyList())
        val isLoading = viewModel.isLoading.collectAsState()
        val isError = viewModel.isError.collectAsState()
        ServerListCompose(
            serverItems = serverList.value,
            isLoading = isLoading.value,
            isError = isError.value,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setUp()
    }
}
