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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
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
    private val profile: Flow<Outfit?> by lazy {
        pS2LinkRepository.getOutfitAsFlow(outfitId, namespace).onEach {
            if (it == null) {
                _displayAddOutfit.value = false
                _displayRemoveOutfit.value = false
                _displayPreferOutfit.value = false
                _displayUnpreferOutfit.value = false
                return@onEach
            }

            if (it.cached) {
                _displayAddOutfit.value = false
                _displayRemoveOutfit.value = true
            } else {
                _displayAddOutfit.value = true
                _displayRemoveOutfit.value = false
            }
            _outfit = it
            refreshPreferredOutfitState()
        }
    }
    private val _displayAddOutfit = MutableStateFlow(false)
    private val _displayRemoveOutfit = MutableStateFlow(false)
    private val _displayPreferOutfit = MutableStateFlow(false)
    private val _displayUnpreferOutfit = MutableStateFlow(false)
    val title: Flow<String?> by lazy { profile.map { it?.name } }
    val displayAddOutfit = _displayAddOutfit.asStateFlow()
    val displayRemoveOutfit = _displayRemoveOutfit.asStateFlow()
    val displayPreferOutfit = _displayPreferOutfit.asStateFlow()
    val displayUnpreferOutfit = _displayUnpreferOutfit.asStateFlow()

    private var _outfit: Outfit? = null
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
    }

    suspend fun addOutfit() = withContext(dispatcherProvider.ioDispatcher()) {
        val lang = ps2Settings.getCurrentLang() ?: CensusLang.EN
        val outfit = pS2LinkRepository.getOutfit(outfitId, namespace, lang)
        if (outfit == null) {
            // TODO : Report error
            return@withContext
        }
        pS2LinkRepository.saveOutfit(outfit.copy(cached = true))
    }

    suspend fun removeOutfit() = withContext(dispatcherProvider.ioDispatcher()) {
        val lang = ps2Settings.getCurrentLang() ?: CensusLang.EN
        val outfit = pS2LinkRepository.getOutfit(outfitId, namespace, lang)
        if (outfit == null) {
            // TODO : Report error
            return@withContext
        }
        pS2LinkRepository.saveOutfit(outfit.copy(cached = false))
    }

    suspend fun pinOutfit() = withContext(dispatcherProvider.ioDispatcher()) {
        ps2Settings.updatePreferredOutfitNamespace(namespace)
        ps2Settings.updatePreferredOutfitId(outfitId)
        refreshPreferredOutfitState()
    }

    suspend fun unpinOutfit() = withContext(dispatcherProvider.ioDispatcher()) {
        ps2Settings.updatePreferredOutfitNamespace(null)
        ps2Settings.updatePreferredOutfitId(null)
        refreshPreferredOutfitState()
    }

    private suspend fun refreshPreferredOutfitState() {
        val preferredNamespace = ps2Settings.getPreferredOutfitNamespace()
        val preferredOutfit = ps2Settings.getPreferredOutfitId()

        if (preferredNamespace == _outfit?.namespace && _outfit?.id == preferredOutfit) {
            _displayPreferOutfit.value = false
            _displayUnpreferOutfit.value = true
        } else {
            _displayPreferOutfit.value = true
            _displayUnpreferOutfit.value = false
        }
    }
}
