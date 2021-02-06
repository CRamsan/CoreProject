package com.cesarandres.ps2link.fragments.serverlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.fragment.app.viewModels
import com.cesarandres.ps2link.R
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
        ServerListCompose(
            serverItems = serverList.value,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.setUp()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        setHasOptionsMenu(true)
        requireActivity().title = getString(R.string.title_profiles)
        return view
    }
}
