package com.cramsan.ps2link.appfrontend.profilepager.profile

import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.logging.logE
import com.cramsan.framework.logging.logW
import com.cramsan.ps2link.appcore.network.isSuccessfulAndContainsBody
import com.cramsan.ps2link.appcore.network.requireBody
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import com.cramsan.ps2link.appfrontend.BasePS2Event
import com.cramsan.ps2link.appfrontend.BasePS2ViewModel
import com.cramsan.ps2link.appfrontend.BasePS2ViewModelInterface
import com.cramsan.ps2link.appfrontend.LanguageProvider
import com.cramsan.ps2link.appfrontend.formatSimpleDateTime
import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.core.models.Namespace
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 *
 */
class ProfileViewModel(
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
    ProfileViewModelInterface {

    override val logTag: String
        get() = "ProfileViewModel"

    private var characterId: String? = null
    private var namespace: Namespace? = null

    // State
    private val _profile = MutableStateFlow<CharacterUIModel?>(null)
    override val profile: StateFlow<CharacterUIModel?> = _profile

    private val _prestigeIcon = MutableStateFlow<String?>(null)
    /**
     * Flow that emits the URL for the prestige badge image.
     */
    override val prestigeIcon: StateFlow<String?> = _prestigeIcon

    private var collectionJob: Job? = null
    private var job: Job? = null

    init {
        println("Initializing VM Profile")
        viewModelScope.launch {
            _profile.onEach {
                println(it)
                println("VM State Profile: $it")
            }.collect()
        }
    }

    override fun setUp(characterId: String?, namespace: Namespace?) {
        if (this.characterId != characterId || this.namespace != namespace) {
            _profile.value = null
        }

        if (characterId == null || namespace == null) {
            logE(logTag, "Invalid arguments: characterId=$characterId namespace=$namespace")
            loadingCompletedWithError()
            return
        }

        this.characterId = characterId
        this.namespace = namespace

        val profileCore = pS2LinkRepository.getCharacterAsFlow(characterId, namespace)
        collectionJob?.cancel()
        collectionJob = profileCore.onEach {
            val uiModel = it?.toUIModel()
            _profile.value = uiModel
            onCharacterUpdated(it)
        }.launchIn(viewModelScope)

        onRefreshRequested()
    }

    private suspend fun onCharacterUpdated(character: Character?) {
        val namespace = namespace

        if (namespace == null || character == null) {
            logW("ProfileViewModel", "Required properties are null")
            return
        }

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
            _events.emit(BasePS2Event.OpenOutfit(outfitId, namespace))
        }
    }

    override fun onRefreshRequested() {
        val characterId = characterId
        val namespace = namespace
        if (namespace == null || characterId == null) {
            logW("ProfileViewModel", "Required properties are null")
            return
        }

        loadingStarted()
        job?.cancel()
        job = viewModelScope.launch(dispatcherProvider.ioDispatcher()) {
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
        creationTime = creationTime?.let { formatSimpleDateTime(it) },
        sessionCount = sessionCount,
        lastLogin = lastLogin?.let { formatSimpleDateTime(it) },
        timePlayed = timePlayed,
        faction = faction,
        server = server,
        outfit = outfit,
        outfitRank = outfitRank,
        namespace = namespace,
        lastUpdate = lastUpdate?.let { formatSimpleDateTime(it) },
        cached = cached,
    )
}

/**
 *
 */
interface ProfileViewModelInterface : BasePS2ViewModelInterface {
    /**
     * Flow that emits the URL for the prestige badge image.
     */
    val prestigeIcon: StateFlow<String?>

    val profile: StateFlow<CharacterUIModel?>
    /**
     *
     */
    fun setUp(characterId: String?, namespace: Namespace?)
    /**
     *
     */
    fun onOutfitSelected(outfitId: String, namespace: Namespace)
    /**
     *
     */
    fun onRefreshRequested()
}