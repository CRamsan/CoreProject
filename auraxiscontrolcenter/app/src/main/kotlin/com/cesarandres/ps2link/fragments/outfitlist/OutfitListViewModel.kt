package com.cesarandres.ps2link.fragments.outfitlist

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import com.cesarandres.ps2link.base.BasePS2ViewModel
import com.cesarandres.ps2link.fragments.OpenOutfit
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.thread.assertIsBackgroundThread
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import com.cramsan.ps2link.core.models.Namespace
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class OutfitListViewModel @Inject constructor(
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
    OutfitListEventHandler {

    override val logTag: String
        get() = "OutfitListViewModel"

    // State
    private val _outfitList = pS2LinkRepository.getAllOutfitsAsFlow().map {
        assertIsBackgroundThread()
        it.sortedBy { outfit -> outfit.name?.lowercase() }
    }.flowOn(dispatcherProvider.ioDispatcher())

    val outfitList = _outfitList.asLiveData()

    override fun onOutfitSelected(outfitId: String, namespace: Namespace) {
        events.value = OpenOutfit(outfitId, namespace)
    }
}
