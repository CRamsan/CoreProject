package com.cramsan.ps2link.appfrontend.mainmenu

import com.cramsan.framework.assertlib.assertNotNull
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.userevents.logEvent
import com.cramsan.ps2link.appcore.network.isSuccessfulAndContainsBody
import com.cramsan.ps2link.appcore.network.requireBody
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import com.cramsan.ps2link.appfrontend.BasePS2Event
import com.cramsan.ps2link.appfrontend.BasePS2ViewModel
import com.cramsan.ps2link.appfrontend.BasePS2ViewModelInterface
import com.cramsan.ps2link.appfrontend.LanguageProvider
import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.core.models.Outfit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch

/**
 *
 */
class MainMenuViewModel(
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
    MainMenuEventHandler,
    MainMenuViewModelInterface {

    override val logTag: String
        get() = "MainMenuViewModel"

    // State
    private val _preferredProfileId = MutableStateFlow<String?>(null)
    private val _preferredOutfitId = MutableStateFlow<String?>(null)

    /**
     * Flow that emits a nullable [Character] that represents the preferred/starred account selected by the user.
     */
    override val preferredProfile: Flow<Character?> = _preferredProfileId.transform { profileId ->
        if (profileId.isNullOrBlank()) {
            emit(null)
            logEvent(logTag, EVENT_PREFERRED_PROFILE, mapOf(PARAMETER_PRESENT to false.toString()))
            return@transform
        }
        logEvent(logTag, EVENT_PREFERRED_PROFILE, mapOf(PARAMETER_PRESENT to true.toString()))

        val namespace = ps2Settings.getPreferredProfileNamespace()
        assertNotNull(namespace, logTag, "Namespace cannot be null")

        if (namespace != null) {
            emit(
                Character(
                    profileId,
                    creationTime = null,
                    sessionCount = null,
                    prestige = null,
                    cached = false,
                    namespace = namespace,
                ),
            )
            // TODO: Fix wrong language
            val response = pS2LinkRepository.getCharacter(profileId, namespace, languageProvider.getCurrentLang())
            if (response.isSuccessfulAndContainsBody()) {
                emit(response.requireBody())
            } else {
                emit(null)
            }
        } else {
            emit(null)
        }
    }.flowOn(dispatcherProvider.ioDispatcher())

    /**
     * Flow that emits a nullable [Outfit] that represents the preferred/starred outfit selected by the user.
     */
    override val preferredOutfit: Flow<Outfit?> = _preferredOutfitId.transform { outfitId ->
        if (outfitId.isNullOrBlank()) {
            emit(null)
            logEvent(logTag, EVENT_PREFERRED_OUTFIT, mapOf(PARAMETER_PRESENT to false.toString()))
            return@transform
        }
        logEvent(logTag, EVENT_PREFERRED_OUTFIT, mapOf(PARAMETER_PRESENT to true.toString()))

        val namespace = ps2Settings.getPreferredOutfitNamespace()
        assertNotNull(namespace, logTag, "Namespace cannot be null")

        if (namespace != null) {
            emit(Outfit(outfitId, cached = false, namespace = namespace))
            // TODO: Fix wrong language
            val response = pS2LinkRepository.getOutfit(outfitId, namespace, languageProvider.getCurrentLang())
            if (response.isSuccessful) {
                emit(response.requireBody())
            } else {
                emit(null)
            }
        } else {
            emit(null)
        }
    }.flowOn(dispatcherProvider.ioDispatcher())

    override fun updateUI() {
        viewModelScope.launch {
            _preferredProfileId.value = ps2Settings.getPreferredCharacterId()
            _preferredOutfitId.value = ps2Settings.getPreferredOutfitId()
        }
    }

    override fun onPreferredProfileClick(characterId: String, namespace: Namespace) {
        viewModelScope.launch {
            _events.emit(BasePS2Event.OpenProfile(characterId, namespace))
        }
    }

    override fun onPreferredOutfitClick(outfitId: String, namespace: Namespace) {
        viewModelScope.launch {
            _events.emit(BasePS2Event.OpenOutfit(outfitId, namespace))
        }
    }

    override fun onProfileClick() {
        viewModelScope.launch {
            _events.emit(BasePS2Event.OpenProfileList)
        }
    }

    override fun onServersClick() {
        viewModelScope.launch {
            _events.emit(BasePS2Event.OpenServerList)
        }
    }

    override fun onOutfitsClick() {
        viewModelScope.launch {
            _events.emit(BasePS2Event.OpenOutfitList)
        }
    }

    override fun onTwitterClick() {
        viewModelScope.launch {
            _events.emit(BasePS2Event.OpenTwitter)
        }
    }

    override fun onRedditClick() {
        viewModelScope.launch {
            _events.emit(BasePS2Event.OpenReddit)
        }
    }

    override fun onAboutClick() {
        viewModelScope.launch {
            _events.emit(BasePS2Event.OpenAbout)
        }
    }

    companion object {
        const val EVENT_PREFERRED_PROFILE = "preferredProfile"
        const val EVENT_PREFERRED_OUTFIT = "preferredOutfit"
        const val PARAMETER_PRESENT = "present"
    }
}

/**
 *
 */
interface MainMenuViewModelInterface : BasePS2ViewModelInterface {
    /**
     * Flow that emits a nullable [Character] that represents the preferred/starred account selected by the user.
     */
    val preferredProfile: Flow<Character?>

    /**
     * Flow that emits a nullable [Outfit] that represents the preferred/starred outfit selected by the user.
     */
    val preferredOutfit: Flow<Outfit?>

    /**
     *
     */
    fun updateUI()
}
