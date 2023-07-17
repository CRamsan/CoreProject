package com.cramsan.ps2link.appfrontend.addprofile

import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.ps2link.appcore.network.requireBody
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import com.cramsan.ps2link.appfrontend.BasePS2Event
import com.cramsan.ps2link.appfrontend.BasePS2ViewModel
import com.cramsan.ps2link.appfrontend.BasePS2ViewModelInterface
import com.cramsan.ps2link.appfrontend.LanguageProvider
import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.core.models.Namespace
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

/**
 *
 */
class ProfileAddViewModel(
    pS2LinkRepository: PS2LinkRepository,
    pS2Settings: PS2Settings,
    languageProvider: LanguageProvider,
    dispatcherProvider: DispatcherProvider,
    private val targetNamespaces: List<Namespace>,
) : BasePS2ViewModel(
    pS2LinkRepository,
    pS2Settings,
    languageProvider,
    dispatcherProvider,
),
    ProfileAddViewModelInterface {

    override val logTag: String
        get() = "ProfileListViewModel"

    // State
    private val _profileList = MutableStateFlow<ImmutableList<Character>>(persistentListOf())
    override val profileList = _profileList.asStateFlow()
    private val _searchQuery = MutableStateFlow("")
    override val searchQuery = _searchQuery.asStateFlow()

    private var searchJob: Job? = null

    @OptIn(ExperimentalTime::class)
    override fun onSearchFieldUpdated(searchField: String) {
        searchJob?.cancel()
        searchJob = null
        _searchQuery.value = searchField
        searchJob = viewModelScope.launch(dispatcherProvider.ioDispatcher()) {
            loadingStarted()
            // Add this delay to allow for fast typing to cancel previous requests without hitting the API.
            // This means that there is a 1 extra second of UPL.
            delay(1.seconds)
            val lang = ps2Settings.getCurrentLang() ?: languageProvider.getCurrentLang()
            val response = pS2LinkRepository.searchForCharacter(searchField, lang, targetNamespaces)
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
        viewModelScope.launch {
            _events.emit(BasePS2Event.OpenProfile(profileId, namespace))
        }
    }
}

/**
 *
 */
interface ProfileAddViewModelInterface : BasePS2ViewModelInterface {
    val profileList: StateFlow<ImmutableList<Character>>
    val searchQuery: StateFlow<String>
    /**
     *
     */
    fun onSearchFieldUpdated(searchField: String)

    /**
     *
     */
    fun onProfileSelected(profileId: String, namespace: Namespace)
}
