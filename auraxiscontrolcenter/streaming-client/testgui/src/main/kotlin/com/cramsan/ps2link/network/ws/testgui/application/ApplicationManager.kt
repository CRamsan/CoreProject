package com.cramsan.ps2link.network.ws.testgui.application

import com.cramsan.framework.logging.logD
import com.cramsan.framework.logging.logE
import com.cramsan.framework.logging.logI
import com.cramsan.framework.logging.logW
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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.awt.Desktop
import java.io.File
import javax.swing.JFrame
import kotlin.system.exitProcess

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

    private val applicationState = MutableStateFlow(
        ApplicationUIModel.State(
            screenType = ScreenType.UNDEFINED,
            programMode = ProgramMode.LOADING,
            character = null,
            debugModeEnabled = true,
        ),
    )

    private val trayUIModel = MutableStateFlow(
        ApplicationUIModel.TrayUIModel(
            statusLabel = applicationState.value.programMode.toFriendlyString(),
            actionLabel = applicationState.value.programMode.toActionLabel(),
            iconPath = pathForStatus(ProgramMode.LOADING),
        ),
    )

    private val windowUIModel = MutableStateFlow(
        ApplicationUIModel.WindowUIModel(
            isVisible = false,
            iconPath = "icon_large.png",
        ),
    )

    val uiModel = combine(windowUIModel, trayUIModel, applicationState) { t1, t2, t3 ->
        ApplicationUIModel(t1, t2, t3)
    }.stateIn(
        coroutineScope,
        SharingStarted.Companion.Eagerly,
        ApplicationUIModel(
            windowUIModel = windowUIModel.value,
            trayUIModel = trayUIModel.value,
            state = applicationState.value,
        ),
    )

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

    private fun setProgramMode(programMode: ProgramMode) {
        applicationState.value = applicationState.value.copy(
            programMode = programMode,
        )
        trayUIModel.value = trayUIModel.value.copy(
            statusLabel = programMode.toFriendlyString(),
            actionLabel = programMode.toActionLabel(),
            iconPath = pathForStatus(programMode),
        )
    }

    @Suppress("SwallowedException")
    private suspend fun loadCharacter(characterId: String) {
        setProgramMode(ProgramMode.LOADING)
        val character: Character?
        try {
            logI(TAG, "Trying to fetch data for character $characterId.")
            character = serviceClient.getProfile(
                character_id = characterId,
                namespace = Namespace.PS2PC,
                currentLang = CensusLang.EN,
            ).body
            if (character == null) {
                logE(TAG, "Could not find data for character $characterId.")
                ProgramMode.NOT_CONFIGURED
                return
            }
            setProgramMode(ProgramMode.PAUSED)
        } catch (t: Throwable) {
            logE(TAG, "Error when fetching data for character $characterId.")
            preferences.remove(CHARACTER_PREF_KEY)
            setProgramMode(ProgramMode.NOT_CONFIGURED)
            return
        }

        logI(TAG, "Character $characterId loaded.")
        currentPlayer = character
        applicationState.value = applicationState.value.copy(
            character = character,
        )
        preferences.saveString(CHARACTER_PREF_KEY, character.characterId)
    }

    /**
     * Start listening for events for the selected character.
     */
    private fun startListening() {
        val characterId = currentPlayer?.characterId

        if (characterId == null) {
            logE(TAG, "Cannot start listening. CharacterId is null.")
            return
        }

        isClientReady = false
        setProgramMode(ProgramMode.LOADING)
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
            setProgramMode(ProgramMode.RUNNING)
        }
    }

    /**
     * Pause from listening for events.
     */
    private fun pauseListening() {
        streamingClient.stop()
        setProgramMode(ProgramMode.PAUSED)
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
     * Exit the application.
     */
    fun exitApplication() {
        logI(TAG, "Closing program.")
        exitProcess(0)
    }

    /**
     * Close the main window.
     */
    fun closeWindow() {
        windowUIModel.value = windowUIModel.value.copy(
            isVisible = false,
        )
    }

    /**
     * Open the main window.
     */
    fun openWindow() {
        logD(TAG, "Trying to open window")
        windowUIModel.value = windowUIModel.value.copy(
            isVisible = true,
        )
        window?.toFront()
    }

    /**
     * Execute the single tray action available to the user.
     */
    fun onTrayAction() {
        when (uiModel.value.state.programMode) {
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
        windowUIModel.value = windowUIModel.value.copy(
            isVisible = true,
        )
        applicationState.value = applicationState.value.copy(
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

    fun openFolder(path: String) {
        val directory = File(path)
        if (!directory.exists() || !directory.isDirectory) {
            logW(TAG, "Cannot open directory $path")
            return
        }
        Desktop.getDesktop().open(directory)
    }

    fun changeDebugMode(debugEnabled: Boolean) {
        preferences.saveString(Constants.DEBUG_MODE_PREF_KEY, debugEnabled.toString())
        applicationState.value = applicationState.value.copy(
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
                ProgramMode.PAUSED -> "Not Running"
            }
        }

        private fun ProgramMode.toActionLabel(): String? {
            return when (this) {
                ProgramMode.NOT_CONFIGURED, ProgramMode.LOADING -> null
                ProgramMode.RUNNING -> "Pause"
                ProgramMode.PAUSED -> "Start"
            }
        }
    }
}
