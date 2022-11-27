package com.cramsan.ps2link.network.ws.testgui.application

import com.cramsan.framework.logging.logD
import com.cramsan.framework.logging.logE
import com.cramsan.framework.logging.logI
import com.cramsan.framework.preferences.Preferences
import com.cramsan.ps2link.appcore.census.DBGServiceClient
import com.cramsan.ps2link.core.models.CensusLang
import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.network.ws.StreamingClient
import com.cramsan.ps2link.network.ws.StreamingClientEventHandler
import com.cramsan.ps2link.network.ws.messages.AchievementEarned
import com.cramsan.ps2link.network.ws.messages.BattleRankUp
import com.cramsan.ps2link.network.ws.messages.CharacterSubscribe
import com.cramsan.ps2link.network.ws.messages.ConnectionStateChanged
import com.cramsan.ps2link.network.ws.messages.ContinentLock
import com.cramsan.ps2link.network.ws.messages.ContinentUnlock
import com.cramsan.ps2link.network.ws.messages.Death
import com.cramsan.ps2link.network.ws.messages.EventType
import com.cramsan.ps2link.network.ws.messages.FacilityControl
import com.cramsan.ps2link.network.ws.messages.GainExperience
import com.cramsan.ps2link.network.ws.messages.Heartbeat
import com.cramsan.ps2link.network.ws.messages.ItemAdded
import com.cramsan.ps2link.network.ws.messages.MetagameEvent
import com.cramsan.ps2link.network.ws.messages.PlayerFacilityCapture
import com.cramsan.ps2link.network.ws.messages.PlayerFacilityDefend
import com.cramsan.ps2link.network.ws.messages.PlayerLogin
import com.cramsan.ps2link.network.ws.messages.PlayerLogout
import com.cramsan.ps2link.network.ws.messages.ServerEvent
import com.cramsan.ps2link.network.ws.messages.ServerEventPayload
import com.cramsan.ps2link.network.ws.messages.ServiceMessage
import com.cramsan.ps2link.network.ws.messages.ServiceStateChanged
import com.cramsan.ps2link.network.ws.messages.SkillAdded
import com.cramsan.ps2link.network.ws.messages.SubscriptionConfirmation
import com.cramsan.ps2link.network.ws.messages.UnhandledEvent
import com.cramsan.ps2link.network.ws.messages.VehicleDestroy
import com.cramsan.ps2link.network.ws.testgui.Constants
import com.cramsan.ps2link.network.ws.testgui.hoykeys.HotKeyManager
import com.cramsan.ps2link.network.ws.testgui.ui.ApplicationUIModel
import com.cramsan.ps2link.network.ws.testgui.ui.navigation.ScreenType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.swing.JFrame

/**
 * Manager class that provides high level access to all the functionalities of the application.
 *
 * @author cramsan
 */
class ApplicationManager(
    private val streamingClient: StreamingClient,
    private val hotKeyManager: HotKeyManager,
    private val gameSessionManager: GameSessionManager,
    private val preferences: Preferences,
    private val serviceClient: DBGServiceClient,
    private val coroutineScope: CoroutineScope,
) : StreamingClientEventHandler {

    private var currentPlayer: Character? = null

    private var isClientReady = false

    private var window: JFrame? = null

    private var programMode: ProgramMode = ProgramMode.LOADING
        set(value) {
            _uiModel.value = _uiModel.value.copy(
                status = value.toFriendlyString(),
                actionLabel = value.toActionLabel(),
                trayIconPath = pathForStatus(value),
                programMode = value,
            )
            field = value
        }

    private val _uiModel = MutableStateFlow(
        ApplicationUIModel(
            isWindowOpen = false,
            screenType = ScreenType.UNDEFINED,
            status = programMode.toFriendlyString(),
            actionLabel = programMode.toActionLabel(),
            trayIconPath = pathForStatus(ProgramMode.LOADING),
            windowIconPath = "icon_large.png",
            programMode = programMode,
            character = null,
            debugModeEnabled = true,
        ),
    )
    val uiModel = _uiModel.asStateFlow()

    /**
     * Start the application.
     */
    fun startApplication() {
        initialize()
        // Load all hotkeys
        hotKeyManager.loadFromPreferences()
        // Register for events from the WS client
        streamingClient.registerListener(this)
        // Load the character if stored from a previous session.
        tryLoadingCachedCharacter()
    }

    private fun initialize() {
        val inDebugMode = preferences.loadString(Constants.DEBUG_MODE_PREF_KEY).toBoolean()
        changeDebugMode(inDebugMode)
    }

    @Suppress("SwallowedException")
    private fun tryLoadingCachedCharacter() {
        val characterId = preferences.loadString(CHARACTER_PREF_KEY)
        if (characterId.isNullOrBlank()) {
            ProgramMode.NOT_CONFIGURED
            return
        }

        coroutineScope.launch {
            setCharacter(characterId)
        }
    }

    /**
     * Set the character to be observed based on the provided [characterId].
     */
    fun setCharacter(characterId: String) {
        coroutineScope.launch {
            loadCharacter(characterId)
        }
    }

    @Suppress("SwallowedException")
    private suspend fun loadCharacter(characterId: String) {
        programMode = ProgramMode.LOADING
        var character: Character? = null
        try {
            logI(TAG, "Trying to fetch data for character $characterId.")
            character = serviceClient.getProfile(
                character_id = characterId,
                namespace = Namespace.PS2PC,
                currentLang = CensusLang.EN,
            ).body
            programMode = if (character == null) {
                logE(TAG, "Could not find data for character $characterId.")
                ProgramMode.NOT_CONFIGURED
            } else {
                ProgramMode.PAUSED
            }
        } catch (t: Throwable) {
            logE(TAG, "Error when fetching data for character $characterId.")
            preferences.remove(CHARACTER_PREF_KEY)
            programMode = ProgramMode.NOT_CONFIGURED
        }

        when (programMode) {
            ProgramMode.NOT_CONFIGURED -> return
            ProgramMode.LOADING, ProgramMode.RUNNING -> {
                logE(TAG, "Cannot load character data while program is not paused.")
                return
            }
            ProgramMode.PAUSED -> Unit
        }

        logI(TAG, "Character $characterId loaded.")
        currentPlayer = character
        _uiModel.value = _uiModel.value.copy(
            character = character,
        )
        preferences.saveString(CHARACTER_PREF_KEY, character?.characterId)
    }

    /**
     * Start listening for events for the selected character.
     */
    fun startListening() {
        val characterId = currentPlayer?.characterId

        if (characterId == null) {
            logE(TAG, "Cannot start listening. CharacterId is null.")
            return
        }

        isClientReady = false
        programMode = ProgramMode.LOADING
        coroutineScope.launch {
            streamingClient.start()

            for (i in 0..5) {
                delay(1000)
                logI(TAG, "Waiting for client to be ready to receive events.")
                if (isClientReady) {
                    break
                }
            }

            streamingClient.sendMessage(
                CharacterSubscribe(
                    characters = listOf(characterId),
                    eventNames = listOf(EventType.DEATH),
                ),
            )
            programMode = ProgramMode.RUNNING
        }
    }

    /**
     * Pause from listening for events.
     */
    fun pauseListening() {
        streamingClient.stop()
        programMode = ProgramMode.PAUSED
    }

    override fun onServerEventReceived(serverEvent: ServerEvent) {
        val character = currentPlayer

        if (character == null) {
            logE(TAG, "Received event while character is null.")
            return
        }

        when (serverEvent) {
            is ConnectionStateChanged -> Unit
            is Heartbeat -> Unit
            is ServiceMessage<*> -> { handleServerEventPayload(character, serverEvent.payload) }
            is ServiceStateChanged -> { isClientReady = true }
            is SubscriptionConfirmation -> Unit
            is UnhandledEvent -> Unit
        }
    }

    @Suppress("ComplexMethod")
    private fun handleServerEventPayload(character: Character, payload: ServerEventPayload?) {
        when (payload) {
            is AchievementEarned -> Unit
            is BattleRankUp -> Unit
            is ContinentLock -> Unit
            is ContinentUnlock -> Unit
            is Death -> {
                gameSessionManager.onPlayerDeathEvent(character, payload)
            }
            is FacilityControl -> Unit
            is GainExperience -> Unit
            is ItemAdded -> Unit
            is MetagameEvent -> Unit
            is PlayerFacilityCapture -> Unit
            is PlayerFacilityDefend -> Unit
            is PlayerLogin -> Unit
            is PlayerLogout -> Unit
            is SkillAdded -> Unit
            is VehicleDestroy -> Unit
            null -> Unit
        }
    }

    /**
     * Close the main window.
     */
    fun closeWindow() {
        _uiModel.value = _uiModel.value.copy(
            isWindowOpen = false,
        )
    }

    /**
     * Open the main window.
     */
    fun openWindow() {
        logD(TAG, "Trying to open window")
        _uiModel.value = _uiModel.value.copy(
            isWindowOpen = true,
        )
        window?.toFront()
    }

    /**
     * Execute the single tray action available to the user.
     */
    fun onTrayAction() {
        when (programMode) {
            ProgramMode.NOT_CONFIGURED -> openWindow()
            ProgramMode.LOADING -> openWindow()
            ProgramMode.RUNNING -> pauseListening()
            ProgramMode.PAUSED -> startListening()
        }
    }

    /**
     * Change the screen that is being displayed.
     */
    fun setCurrentScreen(screenType: ScreenType) {
        _uiModel.value = _uiModel.value.copy(
            isWindowOpen = true,
            screenType = screenType,
        )
    }

    /**
     * Register a [window] so we can execute some operations based on the applications state.
     */
    fun registerWindow(window: JFrame) {
        this.window = window
    }

    /**
     * Deregister the referenced window.
     */
    fun deregisterWindow() {
        window = null
    }

    fun changeDebugMode(debugEnabled: Boolean) {
        preferences.saveString(Constants.DEBUG_MODE_PREF_KEY, debugEnabled.toString())
        _uiModel.value = _uiModel.value.copy(
            debugModeEnabled = debugEnabled,
        )
    }

    companion object {

        private const val TAG = "ApplicationManager"

        private const val CHARACTER_PREF_KEY = "lastSelectedPlayer"
        private fun pathForStatus(programMode: ProgramMode) = when (programMode) {
            ProgramMode.NOT_CONFIGURED, ProgramMode.LOADING -> "icon_small.png"
            ProgramMode.RUNNING -> "icon_running.png"
            ProgramMode.PAUSED -> "icon_not_running.png"
        }

        private fun ProgramMode.toFriendlyString(): String {
            return when (this) {
                ProgramMode.NOT_CONFIGURED, ProgramMode.LOADING -> "Not configured"
                ProgramMode.RUNNING -> "Running"
                ProgramMode.PAUSED -> "Paused"
            }
        }

        private fun ProgramMode.toActionLabel(): String? {
            return when (this) {
                ProgramMode.NOT_CONFIGURED, ProgramMode.LOADING -> null
                ProgramMode.RUNNING -> "Pause"
                ProgramMode.PAUSED -> "Resume"
            }
        }
    }
}
