package com.cesarandres.ps2link.fragments.addoutfit

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.cesarandres.ps2link.base.BasePS2ViewModel
import com.cesarandres.ps2link.fragments.OpenOutfit
import com.cesarandres.ps2link.getCurrentLang
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.ps2link.appcore.network.requireBody
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.core.models.Outfit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

@HiltViewModel
class OutfitAddViewModel @Inject constructor(
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
),
    OutfitAddEventHandler {

    override val logTag: String
        get() = "OutfitAddViewModel"

    // State
    private val _outfitList = MutableStateFlow<List<Outfit>>(emptyList())
    private val _tagSearchQuery = MutableStateFlow("")
    private val _nameSearchQuery = MutableStateFlow("")
    private val queryFlow = _tagSearchQuery.combine(_nameSearchQuery) { tag, name ->
        tag to name
    }

    val outfitList = _outfitList.asStateFlow()
    val tagSearchQuery = _tagSearchQuery.asStateFlow()
    val nameSearchQuery = _nameSearchQuery.asStateFlow()

    private var searchJob: Job? = null

    init {
        queryFlow.onEach {
            search(it.first, it.second)
        }.launchIn(ioScope)
    }

    override fun onTagFieldUpdated(searchField: String) {
        _tagSearchQuery.value = searchField
    }

    override fun onNameFieldUpdated(searchField: String) {
        _nameSearchQuery.value = searchField
    }

    @OptIn(ExperimentalTime::class)
    fun search(tag: String, name: String) {
        searchJob?.cancel()
        searchJob = null

        searchJob = ioScope.launch {
            loadingStarted()
            // Add this delay to allow for fast typing to cancel previous requests without hitting the API.
            // This means that there is a 1 extra second of UPL.
            delay(1.seconds)
            val lang = ps2Settings.getCurrentLang() ?: getCurrentLang()
            val response = pS2LinkRepository.searchForOutfits(tag, name, lang)
            if (response.isSuccessful) {
                _outfitList.value = response.requireBody().sortedBy {
                    it.name?.lowercase()
                }
                loadingCompleted()
            } else {
                loadingCompletedWithError()
            }
        }
    }

    override fun onOutfitSelected(outfitId: String, namespace: Namespace) {
        events.value = OpenOutfit(outfitId, namespace)
    }
}
