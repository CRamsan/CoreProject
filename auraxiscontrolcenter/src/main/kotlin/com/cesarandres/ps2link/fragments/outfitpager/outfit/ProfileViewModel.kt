package com.cesarandres.ps2link.fragments.profilepager.profile

import android.app.Application
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.cesarandres.ps2link.base.BasePS2ViewModel
import com.cesarandres.ps2link.fragments.OpenOutfit
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.logging.logE
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import com.cramsan.ps2link.core.models.CensusLang
import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.core.models.Namespace
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ProfileViewModel @ViewModelInject constructor(
    application: Application,
    pS2LinkRepository: PS2LinkRepository,
    pS2Settings: PS2Settings,
    dispatcherProvider: DispatcherProvider,
    @Assisted savedStateHandle: SavedStateHandle,
) : BasePS2ViewModel(
        application,
        pS2LinkRepository,
        pS2Settings,
        dispatcherProvider,
        savedStateHandle
    ),
    ProfileEventHandler {

    override val logTag: String
        get() = "ProfileViewModel"

    // State
    lateinit var profile: Flow<Character?>

    fun setUp(characterId: String?, namespace: Namespace?) {
        if (characterId == null || namespace == null) {
            logE(logTag, "Invalid arguments: characterId=$characterId namespace=$namespace")
            // TODO: Provide some event that can be handled by the UI
            return
        }
        profile = pS2LinkRepository.getCharacterAsFlow(characterId, namespace)
        ioScope.launch {
            val lang = ps2Settings.getCurrentLang() ?: CensusLang.EN
            pS2LinkRepository.getCharacter(characterId, namespace, lang, forceUpdate = true)
        }
    }

    override fun onOutfitSelected(outfitId: String, namespace: Namespace) {
        events.value = OpenOutfit(outfitId, namespace)
    }
}
