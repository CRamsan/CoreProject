package com.cesarandres.ps2link.fragments.profilepager

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.cesarandres.ps2link.base.BasePS2ViewModel
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.logging.logE
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import com.cramsan.ps2link.core.models.CensusLang
import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.core.models.Namespace
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfilePagerViewModel @Inject constructor(
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
        get() = "ProfilePagerViewModel"

    // State
    private var profile: Character? = null
    private lateinit var characterId: String
    private lateinit var namespace: Namespace

    fun setUp(characterId: String?, namespace: Namespace?) {
        if (characterId == null || namespace == null) {
            logE(logTag, "Invalid arguments: characterId=$characterId namespace=$namespace")
            // TODO: Provide some event that can be handled by the UI
            return
        }
        this.characterId = characterId
        this.namespace = namespace
        ioScope.launch {
            val lang = ps2Settings.getCurrentLang() ?: CensusLang.EN
            profile = pS2LinkRepository.getCharacter(characterId, namespace, lang, false)
        }
    }

    fun addCharacter() {
        ioScope.launch {
            val lang = ps2Settings.getCurrentLang() ?: CensusLang.EN
            val character = pS2LinkRepository.getCharacter(characterId, namespace, lang)
            if (character == null) {
                // TODO : Report error
                return@launch
            }
            pS2LinkRepository.saveCharacter(character.copy(cached = true))
        }
    }

    fun removeCharacter() {
        ioScope.launch {
            val lang = ps2Settings.getCurrentLang() ?: CensusLang.EN
            val character = pS2LinkRepository.getCharacter(characterId, namespace, lang)
            if (character == null) {
                // TODO : Report error
                return@launch
            }
            pS2LinkRepository.saveCharacter(character.copy(cached = false))
        }
    }

    fun pinCharacter() {
        ioScope.launch {
            ps2Settings.updatePreferredNamespace(namespace)
            ps2Settings.updatePreferredCharacterId(characterId)
        }
    }

    fun unpinCharacter() {
        ioScope.launch {
            ps2Settings.updatePreferredNamespace(null)
            ps2Settings.updatePreferredCharacterId(null)
        }
    }
}
