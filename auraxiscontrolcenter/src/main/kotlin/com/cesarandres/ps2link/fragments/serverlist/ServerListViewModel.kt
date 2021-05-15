package com.cesarandres.ps2link.fragments.serverlist

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import com.cesarandres.ps2link.base.BasePS2ViewModel
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.thread.assertIsBackgroundThread
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import com.cramsan.ps2link.core.models.CensusLang
import com.cramsan.ps2link.core.models.Server
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServerListViewModel @Inject constructor(
    application: Application,
    pS2LinkRepository: PS2LinkRepository,
    pS2Settings: PS2Settings,
    dispatcherProvider: DispatcherProvider,
    savedStateHandle: SavedStateHandle,
) : BasePS2ViewModel(
    application,
    pS2LinkRepository,
    pS2Settings,
    dispatcherProvider,
    savedStateHandle
) {

    override val logTag: String
        get() = "ServerListViewModel"

    // State
    private val _serverList = MutableStateFlow(emptyList<Server>())
    val serverList = _serverList.asLiveData()

    fun setUp() {
        ioScope.launch {
            downloadServers()
        }
    }

    /**
     * Make an API call to retrieve the list of servers. This will get the
     * current list of servers and their state.
     */
    private suspend fun downloadServers() {
        assertIsBackgroundThread()
        loadingStarted()
        val lang = ps2Settings.getCurrentLang() ?: CensusLang.EN
        val serverList = pS2LinkRepository.getServerList(lang).sortedBy {
            it.serverName?.toLowerCase()
        }
        _serverList.value = serverList
        loadingCompleted()
    }
}
