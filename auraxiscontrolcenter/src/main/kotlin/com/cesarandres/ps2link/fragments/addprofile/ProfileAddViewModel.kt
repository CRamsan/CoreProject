package com.cesarandres.ps2link.fragments.addprofile

import android.app.Application
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.cesarandres.ps2link.base.BasePS2ViewModel
import com.cesarandres.ps2link.fragments.OpenProfile
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.ps2link.appcore.DBGServiceClient
import com.cramsan.ps2link.appcore.dbg.CensusLang
import com.cramsan.ps2link.appcore.dbg.Namespace
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.sqldelight.DbgDAO
import com.cramsan.ps2link.appcore.toCharacter
import com.cramsan.ps2link.db.Character
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileAddViewModel @ViewModelInject constructor(
    application: Application,
    dbgServiceClient: DBGServiceClient,
    dbgDAO: DbgDAO,
    pS2Settings: PS2Settings,
    dispatcherProvider: DispatcherProvider,
    @Assisted savedStateHandle: SavedStateHandle,
) : BasePS2ViewModel(
        application,
        dbgServiceClient,
        dbgDAO,
        pS2Settings,
        dispatcherProvider,
        savedStateHandle
    ),
    ProfileAddEventHandler {

    override val logTag: String
        get() = "ProfileListViewModel"

    // State
    private val _profileList = MutableStateFlow<List<Character>>(emptyList())
    val profileList = _profileList.asStateFlow()
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private var searchJob: Job? = null

    override fun onSearchFieldUpdated(searchField: String) {
        searchJob?.cancel()
        searchJob = null
        _searchQuery.value = searchField
        searchJob = ioScope.launch {
            if (searchField.length < 3) {
                _profileList.value = emptyList()
                return@launch
            }
            loadingStarted()
            val lang = ps2Settings.getCurrentLang() ?: CensusLang.EN
            val profiles = Namespace.values().map { namespace ->
                val job = async {
                    val endpointProfileList = dbgCensus.getProfiles(
                        searchField = searchField,
                        namespace = namespace,
                        currentLang = lang
                    )
                    endpointProfileList?.map {
                        it.toCharacter(namespace)
                    }
                }
                job
            }.awaitAll().filterNotNull().flatten().sortedBy { it.name }
            _profileList.value = profiles
            loadingCompleted()
        }
    }

    override fun onProfileSelected(profileId: String) {
        events.value = OpenProfile(profileId)
    }
}
