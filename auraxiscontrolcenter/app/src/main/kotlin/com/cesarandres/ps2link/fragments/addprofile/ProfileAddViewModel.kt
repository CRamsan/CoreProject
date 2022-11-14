package com.cesarandres.ps2link.fragments.addprofile

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.cesarandres.ps2link.base.BasePS2ViewModel
import com.cesarandres.ps2link.fragments.OpenProfile
import com.cesarandres.ps2link.getCurrentLang
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.ps2link.appcore.network.requireBody
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.core.models.Namespace
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

@HiltViewModel
class ProfileAddViewModel @Inject constructor(
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
    ProfileAddEventHandler {

    override val logTag: String
        get() = "ProfileListViewModel"

    // State
    private val _profileList = MutableStateFlow<ImmutableList<Character>>(persistentListOf())
    val profileList = _profileList.asStateFlow()
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private var searchJob: Job? = null

    @OptIn(ExperimentalTime::class)
    override fun onSearchFieldUpdated(searchField: String) {
        searchJob?.cancel()
        searchJob = null
        _searchQuery.value = searchField
        searchJob = ioScope.launch {
            loadingStarted()
            // Add this delay to allow for fast typing to cancel previous requests without hitting the API.
            // This means that there is a 1 extra second of UPL.
            delay(1.seconds)
            val lang = ps2Settings.getCurrentLang() ?: getCurrentLang()
            val response = pS2LinkRepository.searchForCharacter(searchField, lang)
            if (response.isSuccessful) {
                _profileList.value = response.requireBody().sortedBy {
                    it.name?.lowercase()
                }.toImmutableList()
                loadingCompleted()
            } else {
                loadingCompletedWithError()
            }
        }
    }

    override fun onProfileSelected(profileId: String, namespace: Namespace) {
        events.value = OpenProfile(profileId, namespace)
    }
}
