package com.cesarandres.ps2link.fragments.serverlist

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.cesarandres.ps2link.base.BasePS2ViewModel
import com.cesarandres.ps2link.getCurrentLang
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.thread.assertIsBackgroundThread
import com.cramsan.ps2link.appcore.network.requireBody
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import com.cramsan.ps2link.core.models.Server
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    savedStateHandle,
),
    ServerListEventHandler {

    override val logTag: String
        get() = "ServerListViewModel"

    // State
    private val _serverList = MutableStateFlow<ImmutableList<Server>>(persistentListOf())
    val serverList = _serverList.asStateFlow()

    fun setUp() {
        onRefreshRequested()
    }

    /**
     * Make an API call to retrieve the list of servers. This will get the
     * current list of servers and their state.
     */
    private suspend fun downloadServers() {
        assertIsBackgroundThread()
        loadingStarted()
        val lang = ps2Settings.getCurrentLang() ?: getCurrentLang()
        val response = pS2LinkRepository.getServerList(lang)
        if (response.isSuccessful) {
            val serverList = response.requireBody().sortedBy {
                it.serverName?.lowercase()
            }
            _serverList.value = serverList.toImmutableList()
            loadingCompleted()
        } else {
            loadingCompletedWithError()
        }
    }

    override fun onRefreshRequested() {
        ioScope.launch {
            downloadServers()
        }
    }
}
