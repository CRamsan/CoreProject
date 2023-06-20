package com.cramsan.ps2link.appfrontend.serverlist

import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.thread.assertIsBackgroundThread
import com.cramsan.ps2link.appcore.network.requireBody
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import com.cramsan.ps2link.appfrontend.BasePS2ViewModel
import com.cramsan.ps2link.appfrontend.BasePS2ViewModelInterface
import com.cramsan.ps2link.appfrontend.LanguageProvider
import com.cramsan.ps2link.core.models.Server
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 *
 */
class ServerListViewModel(
    pS2LinkRepository: PS2LinkRepository,
    pS2Settings: PS2Settings,
    languageProvider: LanguageProvider,
    dispatcherProvider: DispatcherProvider,
) : BasePS2ViewModel(
    pS2LinkRepository,
    pS2Settings,
    languageProvider,
    dispatcherProvider,
),
    ServerListEventHandler,
    ServerListViewModelInterface {

    override val logTag: String
        get() = "ServerListViewModel"

    // State
    private val _serverList = MutableStateFlow<ImmutableList<Server>>(persistentListOf())
    override val serverList = _serverList.asStateFlow()

    override fun setUp() {
        onRefreshRequested()
    }

    /**
     * Make an API call to retrieve the list of servers. This will get the
     * current list of servers and their state.
     */
    override suspend fun downloadServers() {
        assertIsBackgroundThread()
        loadingStarted()
        val lang = ps2Settings.getCurrentLang() ?: languageProvider.getCurrentLang()
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
        viewModelScope.launch(dispatcherProvider.ioDispatcher()) {
            downloadServers()
        }
    }
}

/**
 *
 */
interface ServerListViewModelInterface : BasePS2ViewModelInterface {
    val serverList: StateFlow<ImmutableList<Server>>
    /**
     *
     */
    fun setUp()

    /**
     * Make an API call to retrieve the list of servers. This will get the
     * current list of servers and their state.
     */
    suspend fun downloadServers()
}
