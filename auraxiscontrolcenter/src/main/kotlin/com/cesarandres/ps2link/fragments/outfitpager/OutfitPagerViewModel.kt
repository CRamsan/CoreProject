package com.cesarandres.ps2link.fragments.outfitpager

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.cesarandres.ps2link.base.BasePS2ViewModel
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.logging.logE
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import com.cramsan.ps2link.core.models.CensusLang
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.core.models.Outfit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OutfitPagerViewModel @Inject constructor(
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
) {

    override val logTag: String
        get() = "OutfitPagerViewModel"

    // State
    private var outfit: Outfit? = null
    private lateinit var outfitId: String
    private lateinit var namespace: Namespace

    fun setUp(outfitId: String?, namespace: Namespace?) {
        if (outfitId == null || namespace == null) {
            logE(logTag, "Invalid arguments: outfitId=$outfitId namespace=$namespace")
            // TODO: Provide some event that can be handled by the UI
            return
        }
        this.outfitId = outfitId
        this.namespace = namespace
        ioScope.launch {
            val lang = ps2Settings.getCurrentLang() ?: CensusLang.EN
            outfit = pS2LinkRepository.getOutfit(outfitId, namespace, lang, false)
        }
    }

    fun addOutfit() {
        ioScope.launch {
            val lang = ps2Settings.getCurrentLang() ?: CensusLang.EN
            val character = pS2LinkRepository.getCharacter(outfitId, namespace, lang)
            if (character == null) {
                // TODO : Report error
                return@launch
            }
            pS2LinkRepository.saveCharacter(character.copy(cached = true))
        }
    }

    fun removeOutfit() {
        ioScope.launch {
            val lang = ps2Settings.getCurrentLang() ?: CensusLang.EN
            val character = pS2LinkRepository.getCharacter(outfitId, namespace, lang)
            if (character == null) {
                // TODO : Report error
                return@launch
            }
            pS2LinkRepository.saveCharacter(character.copy(cached = false))
        }
    }

    fun pinOutfit() {
        ioScope.launch {
            ps2Settings.updatePreferredNamespace(namespace)
            ps2Settings.updatePreferredOutfitId(outfitId)
        }
    }

    fun unpinOutfit() {
        ioScope.launch {
            ps2Settings.updatePreferredNamespace(null)
            ps2Settings.updatePreferredOutfitId(null)
        }
    }
}
