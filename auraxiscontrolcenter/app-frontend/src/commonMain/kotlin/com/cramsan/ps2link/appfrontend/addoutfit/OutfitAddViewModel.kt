package com.cramsan.ps2link.appfrontend.addoutfit

import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.ps2link.appcore.network.requireBody
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import com.cramsan.ps2link.appfrontend.BasePS2ViewModel
import com.cramsan.ps2link.appfrontend.LanguageProvider
import com.cramsan.ps2link.appfrontend.OpenOutfit
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.core.models.Outfit
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

class OutfitAddViewModel constructor(
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
    OutfitAddEventHandler,
    OutfitAddViewModelInterface {

    override val logTag: String
        get() = "OutfitAddViewModel"

    // State
    private val _outfitList = MutableStateFlow<ImmutableList<Outfit>>(persistentListOf())
    private val _tagSearchQuery = MutableStateFlow("")
    private val _nameSearchQuery = MutableStateFlow("")
    private val queryFlow = _tagSearchQuery.combine(_nameSearchQuery) { tag, name ->
        tag to name
    }

    override val outfitList = _outfitList.asStateFlow()
    override val tagSearchQuery = _tagSearchQuery.asStateFlow()
    override val nameSearchQuery = _nameSearchQuery.asStateFlow()

    private var searchJob: Job? = null

    init {
        queryFlow.onEach {
            search(it.first, it.second)
        }.launchIn(viewModelScope)
    }

    override fun onTagFieldUpdated(searchField: String) {
        _tagSearchQuery.value = searchField
    }

    override fun onNameFieldUpdated(searchField: String) {
        _nameSearchQuery.value = searchField
    }

    @OptIn(ExperimentalTime::class)
    override fun search(tag: String, name: String) {
        searchJob?.cancel()
        searchJob = null

        searchJob = viewModelScope.launch(dispatcherProvider.ioDispatcher()) {
            loadingStarted()
            // Add this delay to allow for fast typing to cancel previous requests without hitting the API.
            // This means that there is a 1 extra second of UPL.
            delay(1.seconds)
            val lang = ps2Settings.getCurrentLang() ?: languageProvider.getCurrentLang()
            val response = pS2LinkRepository.searchForOutfits(tag, name, lang)
            if (response.isSuccessful) {
                _outfitList.value = response.requireBody().sortedBy {
                    it.name?.lowercase()
                }.toImmutableList()
                loadingCompleted()
            } else {
                loadingCompletedWithError()
            }
        }
    }

    override fun onOutfitSelected(outfitId: String, namespace: Namespace) {
        viewModelScope.launch {
            _events.emit(OpenOutfit(outfitId, namespace))
        }
    }
}
