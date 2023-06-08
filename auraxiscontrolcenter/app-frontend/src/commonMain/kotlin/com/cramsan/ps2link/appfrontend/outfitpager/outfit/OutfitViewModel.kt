package com.cramsan.ps2link.appfrontend.outfitpager.outfit

import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.logging.logE
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import com.cramsan.ps2link.appfrontend.BasePS2ViewModel
import com.cramsan.ps2link.appfrontend.FormatSimpleDate
import com.cramsan.ps2link.appfrontend.LanguageProvider
import com.cramsan.ps2link.appfrontend.OpenProfile
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.core.models.Outfit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class OutfitViewModel constructor(
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
    OutfitEventHandler,
    OutfitViewModelInterface {

    override val logTag: String
        get() = "OutfitViewModel"

    private lateinit var outfitId: String
    private lateinit var namespace: Namespace

    // State
    override lateinit var outfit: Flow<OutfitUIModel?>

    override fun setUp(outfitId: String?, namespace: Namespace?) {
        if (outfitId == null || namespace == null) {
            logE(logTag, "Invalid arguments: outfitId=$outfitId namespace=$namespace")
            loadingCompletedWithError()
            return
        }
        this.outfitId = outfitId
        this.namespace = namespace
        outfit = pS2LinkRepository.getOutfitAsFlow(outfitId, namespace).map {
            it?.toUIModel()
        }
        onRefreshRequested()
    }

    override fun onProfileSelected(profileId: String, namespace: Namespace) {
        viewModelScope.launch {
            _events.emit(OpenProfile(profileId, namespace))
        }
    }

    override fun onRefreshRequested() {
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
        timeCreated = timeCreated?.let { FormatSimpleDate(it) },
        leader = leader,
        memberCount = memberCount,
        namespace = namespace,
        cached = cached,
    )
}

interface OutfitViewModelInterface {
    // State
    var outfit: Flow<OutfitUIModel?>
    fun setUp(outfitId: String?, namespace: Namespace?)
}
