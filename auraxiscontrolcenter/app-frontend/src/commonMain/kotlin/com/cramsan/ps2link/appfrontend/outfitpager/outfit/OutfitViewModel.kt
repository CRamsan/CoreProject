package com.cramsan.ps2link.appfrontend.outfitpager.outfit

import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.logging.logE
import com.cramsan.framework.logging.logW
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import com.cramsan.ps2link.appfrontend.BasePS2Event
import com.cramsan.ps2link.appfrontend.BasePS2ViewModel
import com.cramsan.ps2link.appfrontend.BasePS2ViewModelInterface
import com.cramsan.ps2link.appfrontend.LanguageProvider
import com.cramsan.ps2link.appfrontend.formatSimpleDate
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.core.models.Outfit
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 *
 */
class OutfitViewModel(
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
    OutfitViewModelInterface {

    override val logTag: String
        get() = "OutfitViewModel"

    private var outfitId: String? = null
    private var namespace: Namespace? = null

    // State
    private val _outfit = MutableStateFlow<OutfitUIModel?>(null)
    override val outfit: StateFlow<OutfitUIModel?> = _outfit

    private var collectionJob: Job? = null

    override fun setUp(outfitId: String?, namespace: Namespace?) {
        if (outfitId == null || namespace == null) {
            logE(logTag, "Invalid arguments: outfitId=$outfitId namespace=$namespace")
            loadingCompletedWithError()
            return
        }
        this.outfitId = outfitId
        this.namespace = namespace
        val outfitCore = pS2LinkRepository.getOutfitAsFlow(outfitId, namespace)
        collectionJob?.cancel()
        collectionJob = outfitCore.onEach {
            val uiModel = it?.toUIModel()
            _outfit.value = uiModel
        }.launchIn(viewModelScope)
        onRefreshRequested()
    }

    override fun onProfileSelected(profileId: String, namespace: Namespace) {
        viewModelScope.launch {
            _events.emit(BasePS2Event.OpenProfile(profileId, namespace))
        }
    }

    override fun onRefreshRequested() {
        val outfitId = outfitId
        val namespace = namespace

        if (namespace == null || outfitId == null) {
            logW("OutfitViewModel", "Required properties are null")
            return
        }

        loadingStarted()
        viewModelScope.launch(dispatcherProvider.ioDispatcher()) {
            val lang = ps2Settings.getCurrentLang() ?: languageProvider.getCurrentLang()
            if (pS2LinkRepository.getOutfit(outfitId, namespace, lang, forceUpdate = true).isSuccessful) {
                loadingCompleted()
            } else {
                loadingCompletedWithError()
            }
        }
    }
}

private fun Outfit.toUIModel(): OutfitUIModel {
    return OutfitUIModel(
        id = id,
        name = name,
        tag = tag,
        faction = faction,
        server = server,
        timeCreated = timeCreated?.let { formatSimpleDate(it) },
        leader = leader,
        memberCount = memberCount,
        namespace = namespace,
        cached = cached,
    )
}

/**
 *
 */
interface OutfitViewModelInterface : BasePS2ViewModelInterface {
    // State
    val outfit: StateFlow<OutfitUIModel?>
    /**
     *
     */
    fun setUp(outfitId: String?, namespace: Namespace?)
    /**
     *
     */
    fun onProfileSelected(profileId: String, namespace: Namespace)
    /**
     *
     */
    fun onRefreshRequested()
}
