package com.cesarandres.ps2link.fragments.profilepager

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.cesarandres.ps2link.base.BasePS2ViewModel
import com.cesarandres.ps2link.getCurrentLang
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.logging.logE
import com.cramsan.framework.userevents.logEvent
import com.cramsan.ps2link.appcore.network.isSuccessfulAndContainsBody
import com.cramsan.ps2link.appcore.network.requireBody
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.core.models.Namespace
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.time.ExperimentalTime

@HiltViewModel
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
    savedStateHandle,
) {

    override val logTag: String
        get() = "ProfilePagerViewModel"

    // State
    private val profile: Flow<Character?> by lazy {
        pS2LinkRepository.getCharacterAsFlow(characterId, namespace).onEach {
            if (it == null) {
                _displayAddCharacter.value = false
                _displayRemoveCharacter.value = false
                _displayPreferProfile.value = false
                _displayUnpreferProfile.value = false
                return@onEach
            }

            if (it.cached) {
                _displayAddCharacter.value = false
                _displayRemoveCharacter.value = true
            } else {
                _displayAddCharacter.value = true
                _displayRemoveCharacter.value = false
            }
            _profile = it
            refreshPreferredCharacterState()
        }
    }
    private val _displayAddCharacter = MutableStateFlow(false)
    private val _displayRemoveCharacter = MutableStateFlow(false)
    private val _displayPreferProfile = MutableStateFlow(false)
    private val _displayUnpreferProfile = MutableStateFlow(false)
    val title: Flow<String?> by lazy { profile.map { it?.name } }
    val displayAddCharacter = _displayAddCharacter.asStateFlow()
    val displayRemoveCharacter = _displayRemoveCharacter.asStateFlow()
    val displayPreferProfile = _displayPreferProfile.asStateFlow()
    val displayUnpreferProfile = _displayUnpreferProfile.asStateFlow()

    private var _profile: Character? = null
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
    }

    suspend fun addCharacter() = withContext(dispatcherProvider.ioDispatcher()) {
        logEvent(logTag, "addCharacter")
        val lang = ps2Settings.getCurrentLang() ?: getCurrentLang()
        val response = pS2LinkRepository.getCharacter(characterId, namespace, lang)
        if (response.isSuccessfulAndContainsBody()) {
            val character = response.requireBody()
            @OptIn(ExperimentalTime::class)
            character?.let {
                pS2LinkRepository.saveCharacter(character.copy(cached = true))
            }
        }
    }

    suspend fun removeCharacter() = withContext(dispatcherProvider.ioDispatcher()) {
        logEvent(logTag, "removeCharacter")
        val lang = ps2Settings.getCurrentLang() ?: getCurrentLang()
        val response = pS2LinkRepository.getCharacter(characterId, namespace, lang)
        if (response.isSuccessfulAndContainsBody()) {
            val character = response.requireBody()
            @OptIn(ExperimentalTime::class)
            character?.let {
                pS2LinkRepository.saveCharacter(character.copy(cached = false))
            }
        }
    }

    suspend fun pinCharacter() = withContext(dispatcherProvider.ioDispatcher()) {
        logEvent(logTag, "pinCharacter")
        ps2Settings.updatePreferredProfileNamespace(namespace)
        ps2Settings.updatePreferredCharacterId(characterId)
        refreshPreferredCharacterState()
    }

    suspend fun unpinCharacter() = withContext(dispatcherProvider.ioDispatcher()) {
        logEvent(logTag, "unpinCharacter")
        ps2Settings.updatePreferredProfileNamespace(null)
        ps2Settings.updatePreferredCharacterId(null)
        refreshPreferredCharacterState()
    }

    private suspend fun refreshPreferredCharacterState() {
        val preferredNamespace = ps2Settings.getPreferredProfileNamespace()
        val preferredProfile = ps2Settings.getPreferredCharacterId()

        if (preferredNamespace == _profile?.namespace && _profile?.characterId == preferredProfile) {
            _displayPreferProfile.value = false
            _displayUnpreferProfile.value = true
        } else {
            _displayPreferProfile.value = true
            _displayUnpreferProfile.value = false
        }
    }
}
