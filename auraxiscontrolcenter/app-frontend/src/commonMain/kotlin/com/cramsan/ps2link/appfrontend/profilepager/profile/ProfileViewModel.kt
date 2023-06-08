package com.cramsan.ps2link.appfrontend.profilepager.profile

import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.logging.logE
import com.cramsan.ps2link.appcore.network.isSuccessfulAndContainsBody
import com.cramsan.ps2link.appcore.network.requireBody
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import com.cramsan.ps2link.appfrontend.BasePS2ViewModel
import com.cramsan.ps2link.appfrontend.FormatSimpleDateTime
import com.cramsan.ps2link.appfrontend.LanguageProvider
import com.cramsan.ps2link.appfrontend.OpenOutfit
import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.core.models.Namespace
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ProfileViewModel constructor(
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
    ProfileEventHandler,
    ProfileViewModelInterface {

    override val logTag: String
        get() = "ProfileViewModel"

    private lateinit var characterId: String
    private lateinit var namespace: Namespace
    private lateinit var profileCore: Flow<Character?>

    // State
    override lateinit var profile: Flow<CharacterUIModel?>

    private val _prestigeIcon = MutableStateFlow<String?>(null)
    /**
     * Flow that emits the URL for the prestige badge image.
     */
    override val prestigeIcon: StateFlow<String?> = _prestigeIcon

    override fun setUp(characterId: String?, namespace: Namespace?) {
        if (characterId == null || namespace == null) {
            logE(logTag, "Invalid arguments: characterId=$characterId namespace=$namespace")
            loadingCompletedWithError()
            return
        }

        this.characterId = characterId
        this.namespace = namespace

        profileCore = pS2LinkRepository.getCharacterAsFlow(characterId, namespace)

        profile = profileCore.map { it?.toUIModel() }
        profileCore.onEach {
            it ?: return@onEach

            onCharacterUpdated(it)
        }.launchIn(viewModelScope)

        onRefreshRequested()
    }

    private suspend fun onCharacterUpdated(character: Character) {
        character.battleRank?.let {
            val rankResponse = pS2LinkRepository.getExperienceRank(
                it.toInt(),
                0, // We are going to only render the ranks for pre-prestige.
                character.faction,
                namespace,
                ps2Settings.getCurrentLang() ?: languageProvider.getCurrentLang(),
            )
            if (!rankResponse.isSuccessfulAndContainsBody()) {
                _prestigeIcon.value = null
                return@let
            }
            val rank = rankResponse.requireBody()
            _prestigeIcon.value = rank?.imagePath
        }
    }

    override fun onOutfitSelected(outfitId: String, namespace: Namespace) {
        viewModelScope.launch {
            _events.emit(OpenOutfit(outfitId, namespace))
        }
    }

    override fun onRefreshRequested() {
        loadingStarted()
        viewModelScope.launch(dispatcherProvider.ioDispatcher()) {
            val lang = ps2Settings.getCurrentLang() ?: languageProvider.getCurrentLang()
            if (pS2LinkRepository.getCharacter(
                    characterId,
                    namespace,
                    lang,
                    forceUpdate = true,
                ).isSuccessfulAndContainsBody()
            ) {
                loadingCompleted()
            } else {
                loadingCompletedWithError()
            }
        }
    }
}

private fun Character.toUIModel(): CharacterUIModel {
    return CharacterUIModel(
        characterId = characterId,
        name = name,
        activeProfileId = activeProfileId,
        loginStatus = loginStatus,
        certs = certs,
        battleRank = battleRank,
        prestige = prestige,
        percentageToNextCert = percentageToNextCert,
        percentageToNextBattleRank = percentageToNextBattleRank,
        creationTime = creationTime?.let { FormatSimpleDateTime(it) },
        sessionCount = sessionCount,
        lastLogin = lastLogin?.let { FormatSimpleDateTime(it) },
        timePlayed = timePlayed,
        faction = faction,
        server = server,
        outfit = outfit,
        outfitRank = outfitRank,
        namespace = namespace,
        lastUpdate = lastUpdate?.let { FormatSimpleDateTime(it) },
        cached = cached,
    )
}

interface ProfileViewModelInterface {
    /**
     * Flow that emits the URL for the prestige badge image.
     */
    val prestigeIcon: StateFlow<String?>

    var profile: Flow<CharacterUIModel?>
    fun setUp(characterId: String?, namespace: Namespace?)
}
