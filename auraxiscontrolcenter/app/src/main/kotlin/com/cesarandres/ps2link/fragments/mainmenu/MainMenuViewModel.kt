package com.cesarandres.ps2link.fragments.mainmenu

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.cesarandres.ps2link.base.BasePS2ViewModel
import com.cesarandres.ps2link.fragments.OpenAbout
import com.cesarandres.ps2link.fragments.OpenOutfit
import com.cesarandres.ps2link.fragments.OpenOutfitList
import com.cesarandres.ps2link.fragments.OpenProfile
import com.cesarandres.ps2link.fragments.OpenProfileList
import com.cesarandres.ps2link.fragments.OpenReddit
import com.cesarandres.ps2link.fragments.OpenServerList
import com.cesarandres.ps2link.fragments.OpenTwitter
import com.cesarandres.ps2link.getCurrentLang
import com.cramsan.framework.assertlib.assertNotNull
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.userevents.logEvent
import com.cramsan.ps2link.appcore.network.requireBody
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.core.models.Outfit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainMenuViewModel @Inject constructor(
    application: Application,
    pS2LinkRepository: PS2LinkRepository,
    pS2Settings: PS2Settings,
    dispatcherProvider: DispatcherProvider,
    savedStateHandle: SavedStateHandle
) : BasePS2ViewModel(
    application,
    pS2LinkRepository,
    pS2Settings,
    dispatcherProvider,
    savedStateHandle
),
    MainMenuEventHandler {

    override val logTag: String
        get() = "MainMenuViewModel"

    // State
    private val _preferredProfileId = MutableStateFlow<String?>(null)
    private val _preferredOutfitId = MutableStateFlow<String?>(null)

    /**
     * Flow that emits a nullable [Character] that represents the preferred/starred account selected by the user.
     */
    val preferredProfile: Flow<Character?> = _preferredProfileId.transform { profileId ->
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
                )
            )
            // TODO: Fix wrong language
            val response = pS2LinkRepository.getCharacter(profileId, namespace, getCurrentLang())
            if (response.isSuccessful) {
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
    val preferredOutfit: Flow<Outfit?> = _preferredOutfitId.transform { outfitId ->
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
            val response = pS2LinkRepository.getOutfit(outfitId, namespace, getCurrentLang())
            if (response.isSuccessful) {
                emit(response.requireBody())
            } else {
                emit(null)
            }
        } else {
            emit(null)
        }
    }.flowOn(dispatcherProvider.ioDispatcher())

    fun updateUI() {
        viewModelScope.launch {
            _preferredProfileId.value = ps2Settings.getPreferredCharacterId()
            _preferredOutfitId.value = ps2Settings.getPreferredOutfitId()
        }
    }

    override fun onPreferredProfileClick(characterId: String, namespace: Namespace) {
        events.value = OpenProfile(characterId, namespace)
    }

    override fun onPreferredOutfitClick(outfitId: String, namespace: Namespace) {
        events.value = OpenOutfit(outfitId, namespace)
    }

    override fun onProfileClick() {
        events.value = OpenProfileList
    }

    override fun onServersClick() {
        events.value = OpenServerList
    }

    override fun onOutfitsClick() {
        events.value = OpenOutfitList
    }

    override fun onTwitterClick() {
        events.value = OpenTwitter
    }

    override fun onRedditClick() {
        events.value = OpenReddit
    }

    override fun onAboutClick() {
        events.value = OpenAbout
    }

    companion object {
        const val EVENT_PREFERRED_PROFILE = "preferredProfile"
        const val EVENT_PREFERRED_OUTFIT = "preferredOutfit"
        const val PARAMETER_PRESENT = "present"
    }
}
