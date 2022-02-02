package com.cesarandres.ps2link.fragments.profilepager.profile

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.cesarandres.ps2link.base.BasePS2ViewModel
import com.cesarandres.ps2link.fragments.OpenOutfit
import com.cesarandres.ps2link.getCurrentLang
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.logging.logE
import com.cramsan.ps2link.appcore.network.requireBody
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.core.models.Namespace
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
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
    ProfileEventHandler {

    override val logTag: String
        get() = "ProfileViewModel"

    private lateinit var characterId: String
    private lateinit var namespace: Namespace

    // State
    lateinit var profile: Flow<Character?>

    private val _prestigeIcon = MutableStateFlow<String?>(null)
    /**
     * Flow that emits the URL for the prestige badge image.
     */
    val prestigeIcon: StateFlow<String?> = _prestigeIcon

    fun setUp(characterId: String?, namespace: Namespace?) {
        if (characterId == null || namespace == null) {
            logE(logTag, "Invalid arguments: characterId=$characterId namespace=$namespace")
            loadingCompletedWithError()
            return
        }

        this.characterId = characterId
        this.namespace = namespace

        profile = pS2LinkRepository.getCharacterAsFlow(characterId, namespace)
        profile.onEach {
            it ?: return@onEach

            onCharacterUpdated(it)
        }.launchIn(ioScope)

        onRefreshRequested()
    }

    private suspend fun onCharacterUpdated(character: Character) {
        character.battleRank?.let {
            val rankResponse = pS2LinkRepository.getExperienceRank(
                it.toInt(),
                0, // We are going to only render the ranks for pre-prestige.
                character.faction,
                namespace,
                ps2Settings.getCurrentLang() ?: getCurrentLang(),
            )
            val rank = rankResponse.requireBody() ?: return@let
            _prestigeIcon.value = rank.imagePath
        }
    }

    override fun onOutfitSelected(outfitId: String, namespace: Namespace) {
        events.value = OpenOutfit(outfitId, namespace)
    }

    override fun onRefreshRequested() {
        loadingStarted()
        ioScope.launch {
            val lang = ps2Settings.getCurrentLang() ?: getCurrentLang()
            if (pS2LinkRepository.getCharacter(characterId, namespace, lang, forceUpdate = true).isSuccessful) {
                loadingCompleted()
            } else {
                loadingCompletedWithError()
            }
        }
    }
}
