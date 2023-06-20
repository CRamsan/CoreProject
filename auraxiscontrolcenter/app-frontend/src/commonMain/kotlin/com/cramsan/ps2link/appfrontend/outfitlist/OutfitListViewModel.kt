package com.cramsan.ps2link.appfrontend.outfitlist

import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.thread.assertIsBackgroundThread
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import com.cramsan.ps2link.appfrontend.BasePS2Event
import com.cramsan.ps2link.appfrontend.BasePS2ViewModel
import com.cramsan.ps2link.appfrontend.BasePS2ViewModelInterface
import com.cramsan.ps2link.appfrontend.LanguageProvider
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.core.models.Outfit
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 *
 */
class OutfitListViewModel(
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
    OutfitListEventHandler,
    OutfitListViewModelInterface {

    override val logTag: String
        get() = "OutfitListViewModel"

    // State
    override val outfitList = pS2LinkRepository.getAllOutfitsAsFlow().map {
        assertIsBackgroundThread()
        it.sortedBy { outfit -> outfit.name?.lowercase() }.toImmutableList()
    }.flowOn(dispatcherProvider.ioDispatcher())

    override fun onOutfitSelected(outfitId: String, namespace: Namespace) {
        viewModelScope.launch {
            _events.emit(BasePS2Event.OpenOutfit(outfitId, namespace))
        }
    }
}

/**
 *
 */
interface OutfitListViewModelInterface : BasePS2ViewModelInterface {
    // State
    val outfitList: Flow<ImmutableList<Outfit>>
}
