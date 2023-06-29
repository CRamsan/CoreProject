package com.cramsan.ps2link.network.ws.testgui.ui.screens.tracker

import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.logging.logE
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import com.cramsan.ps2link.appfrontend.BasePS2Event
import com.cramsan.ps2link.appfrontend.BasePS2ViewModel
import com.cramsan.ps2link.appfrontend.BasePS2ViewModelInterface
import com.cramsan.ps2link.appfrontend.LanguageProvider
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.network.ws.messages.ServerEventPayload
import com.cramsan.ps2link.network.ws.testgui.application.ApplicationManager
import com.cramsan.ps2link.network.ws.testgui.application.ApplicationManagerCallback
import com.cramsan.ps2link.network.ws.testgui.application.ProgramMode
import com.cramsan.ps2link.network.ws.testgui.application.toActionLabel
import com.cramsan.ps2link.network.ws.testgui.filelogger.FileLog
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.Queue
import java.util.concurrent.ArrayBlockingQueue

/**
 *
 */
class TrackerViewModel(
    pS2LinkRepository: PS2LinkRepository,
    pS2Settings: PS2Settings,
    private val applicationManager: ApplicationManager,
    languageProvider: LanguageProvider,
    dispatcherProvider: DispatcherProvider,
) : BasePS2ViewModel(
    pS2LinkRepository,
    pS2Settings,
    languageProvider,
    dispatcherProvider,
),
    TrackerViewModelInterface {

    override val logTag: String
        get() = "TrackerViewModel"

    private var characterId: String? = null
    private var namespace: Namespace? = null

    // State
    private val _uiModel = MutableStateFlow(
        TrackerUIModel(
            actionLabel = null,
            profileName = null,
            events = emptyList(),
        )
    )
    override val uiModel: StateFlow<TrackerUIModel> = _uiModel

    private val messageQueue: Queue<PlayerEventUIModel> = ArrayBlockingQueue(MAX_CAPACITY)

    private var collectionJob: Job? = null
    private var job: Job? = null

    init {
        updateUI(applicationManager.uiModel.value.state.programMode)
        applicationManager.registerCallback(object : ApplicationManagerCallback {
            override fun onServerEventPayload(payload: ServerEventPayload) = Unit
            override fun onProgramModeChanged(programMode: ProgramMode) {
                updateUI(programMode)
            }
            override fun onCharacterSelected(characterId: String, namespace: Namespace) = Unit
            override fun onOutfitSelected(outfitId: String, namespace: Namespace) = Unit
            override fun onTrackedCharacterSelected(characterId: String, namespace: Namespace) {
                setUp(characterId, namespace)
            }

            override fun onFileLogActive(fileLog: FileLog) {
                setUpFileLog(fileLog)
            }
        })
    }

    fun setUpFileLog(fileLog: FileLog) {
        job?.cancel()
        job = viewModelScope.launch {
            fileLog.observeLines().collect {
                handleNewLine(it)
            }
        }
    }

    private fun handleNewLine(playerEvent: PlayerEvent) {
        val currentTrackedPlayer = characterId ?: return
        val currentTrackedNamespace = namespace ?: return

        if (messageQueue.size >= MAX_CAPACITY) {
            messageQueue.remove()
        }

        val event = playerEvent.toUIModel(
            currentTrackedPlayer,
            currentTrackedNamespace,
        ) ?: return
        messageQueue.add(event)

        _uiModel.value = _uiModel.value.copy(
            events = messageQueue.toList().asReversed()
        )
    }

    override fun setUp(characterId: String?, namespace: Namespace?) {
        if (this.characterId != characterId || this.namespace != namespace) {
            _uiModel.value = TrackerUIModel(
                actionLabel = null,
                profileName = null,
                events = emptyList(),
            )
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
            _uiModel.value = _uiModel.value.copy(
                profileName = it?.name
            )
        }.launchIn(viewModelScope)
    }

    private fun updateUI(programMode: ProgramMode) {
        when (programMode) {
            ProgramMode.LOADING -> loadingStarted()
            ProgramMode.NOT_CONFIGURED,
            ProgramMode.RUNNING,
            ProgramMode.PAUSED, -> loadingCompleted()
        }

        val actionLabel = programMode.toActionLabel()

        _uiModel.value = _uiModel.value.copy(
            actionLabel = actionLabel
        )
    }
    override fun onProfileSelected(profileId: String, namespace: Namespace) {
        viewModelScope.launch {
            _events.emit(BasePS2Event.OpenProfile(profileId, namespace))
        }
    }

    override fun onPrimaryActionSelected() {
        applicationManager.onTrayAction()
    }

    override fun onOpenSessionFileSelected() {
        applicationManager.openFolder(".")
    }

    companion object {
        private const val MAX_CAPACITY = 100
    }
}

@Suppress("CyclomaticComplexMethod")
private fun PlayerEvent.toUIModel(trackedCharacterId: String, namespace: Namespace): PlayerEventUIModel? {

    var linkedCharacter: String? = null

    val event = when (this) {
        is PlayerEvent.AchievementEarned -> "Achievement Earned"
        is PlayerEvent.BattleRankUp -> "Battle Rank Up"
        is PlayerEvent.Death -> {
            // Killed another player
            if (characterId != trackedCharacterId) {
                linkedCharacter = trackedCharacterId
                "Killed"
            } else {
                // We died
                linkedCharacter = attackerCharacterId
                if (attackerCharacterId == trackedCharacterId) {
                    "Suicide"
                } else {
                    "Death"
                }
            }
        }
        is PlayerEvent.GainExperience -> return null
        is PlayerEvent.ItemAdded -> return null
        is PlayerEvent.PlayerFacilityCapture -> return null
        is PlayerEvent.PlayerFacilityDefend -> return null
        is PlayerEvent.PlayerLogin -> return null
        is PlayerEvent.PlayerLogout -> return null
        is PlayerEvent.SkillAdded -> return null
        is PlayerEvent.VehicleDestroy -> return null
    }

    val description = when (this) {
        is PlayerEvent.AchievementEarned -> ""
        is PlayerEvent.BattleRankUp -> "BR:$battleRank"
        is PlayerEvent.Death -> {
            if (isHeadshot == "1") {
                "Headhost"
            } else {
                ""
            }
        }
        else -> ""
    }

    return PlayerEventUIModel(
        event = event,
        description = description,
        characterId = linkedCharacter,
        namespace = namespace
    )
}

/**
 *
 */
interface TrackerViewModelInterface : BasePS2ViewModelInterface {
    /**
     *
     */
    fun setUp(characterId: String?, namespace: Namespace?)
    /**
     *
     */
    fun onProfileSelected(profileId: String, namespace: Namespace)
    /**
     *
     */
    fun onPrimaryActionSelected()
    /**
     *
     */
    fun onOpenSessionFileSelected()

    val uiModel: StateFlow<TrackerUIModel>
}