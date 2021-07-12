package com.cesarandres.ps2link.fragments.outfitpager.outfit

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.cesarandres.ps2link.base.BasePS2ViewModel
import com.cesarandres.ps2link.fragments.OpenProfile
import com.cesarandres.ps2link.getCurrentLang
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.logging.logE
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.core.models.Outfit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OutfitViewModel @Inject constructor(
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
    OutfitEventHandler {

    override val logTag: String
        get() = "OutfitViewModel"

    private lateinit var outfitId: String
    private lateinit var namespace: Namespace

    // State
    lateinit var outfit: Flow<Outfit?>

    fun setUp(outfitId: String?, namespace: Namespace?) {
        if (outfitId == null || namespace == null) {
            logE(logTag, "Invalid arguments: outfitId=$outfitId namespace=$namespace")
            loadingCompletedWithError()
            return
        }
        this.outfitId = outfitId
        this.namespace = namespace
        outfit = pS2LinkRepository.getOutfitAsFlow(outfitId, namespace)
        onRefreshRequested()
    }

    override fun onProfileSelected(profileId: String, namespace: Namespace) {
        events.value = OpenProfile(profileId, namespace)
    }

    override fun onRefreshRequested() {
        loadingStarted()
        ioScope.launch {
            val lang = ps2Settings.getCurrentLang() ?: getCurrentLang()
            if (pS2LinkRepository.getOutfit(outfitId, namespace, lang, forceUpdate = true).isSuccessful) {
                loadingCompleted()
            } else {
                loadingCompletedWithError()
            }
        }
    }
}