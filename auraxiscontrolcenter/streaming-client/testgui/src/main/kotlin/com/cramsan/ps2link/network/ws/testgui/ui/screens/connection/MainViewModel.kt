package com.cramsan.ps2link.network.ws.testgui.ui.screens.connection

import androidx.compose.runtime.Stable
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.logging.logI
import com.cramsan.ps2link.appcore.census.DBGServiceClient
import com.cramsan.ps2link.core.models.CensusLang
import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.network.ws.messages.Death
import com.cramsan.ps2link.network.ws.messages.PlayerLogin
import com.cramsan.ps2link.network.ws.messages.PlayerLogout
import com.cramsan.ps2link.network.ws.messages.ServerEventPayload
import com.cramsan.ps2link.network.ws.testgui.application.ApplicationManager
import com.cramsan.ps2link.network.ws.testgui.application.ApplicationManagerCallback
import com.cramsan.ps2link.network.ws.testgui.application.ProgramMode
import com.cramsan.ps2link.network.ws.testgui.ui.ApplicationUIModel
import com.cramsan.ps2link.network.ws.testgui.ui.navigation.ScreenType
import com.cramsan.ps2link.network.ws.testgui.ui.screens.BaseViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel to manage the Main screen.
 *
 * @author cramsan
 */
@Stable
class MainViewModel(
    applicationManager: ApplicationManager,
    private val serviceClient: DBGServiceClient,
    dispatcherProvider: DispatcherProvider,
) : BaseViewModel(applicationManager, dispatcherProvider) {

    private var searchJob: Job? = null

    private val _uiState = MutableStateFlow(
        MainUIState(
            isRunning = false,
            characterName = "",
            playerSuggestions = persistentListOf(),
            selectedPlayer = null,
            isListLoading = false,
            isLoading = false,
            isError = false,
            actionLabel = "",
            killList = persistentListOf(),
        ),
    )
    val uiState = _uiState.asStateFlow()

    private val applicationManagerCallback = object : ApplicationManagerCallback {
        override fun onServerEventPayload(character: Character, payload: ServerEventPayload) {
            when (payload) {
                is Death, is PlayerLogin, is PlayerLogout -> fetchKillFeed()
                else -> Unit
            }
        }

        override fun onProgramModeChanged(programMode: ProgramMode) {
            fetchKillFeed()
        }
    }
    override fun onApplicationUIModelUpdated(applicationUIModel: ApplicationUIModel) {
        val isProgramLoading = applicationUIModel.state.programMode == ProgramMode.LOADING
        _uiState.value = _uiState.value.copy(
            selectedPlayer = applicationUIModel.state.character,
            isLoading = isProgramLoading,
            actionLabel = applicationUIModel.trayUIModel.actionLabel ?: "",
        )
    }

    override fun onStart() {
        super.onStart()
        applicationManager.registerCallback(applicationManagerCallback)
    }
    override fun onClose() {
        applicationManager.deregisterCallback(applicationManagerCallback)
        scope?.cancel()
    }

    /**
     * Update the character name to be searched.
     */
    fun updateCharacterName(characterName: String) {
        _uiState.value = _uiState.value.copy(
            characterName = characterName,
            isListLoading = true,
        )

        searchJob?.cancel()
        searchJob = scope?.launch(dispatcherProvider.ioDispatcher()) {
            delay(1000)
            val names = serviceClient.getProfiles(
                searchField = characterName,
                namespace = Namespace.PS2PC,
                CensusLang.EN,
            ).body ?: emptyList()
            _uiState.value = _uiState.value.copy(
                playerSuggestions = names.take(20).toImmutableList(),
                isListLoading = false,
            )
        }
    }

    /**
     */
    private fun fetchKillFeed() {
        logI(TAG, "Fetching kill feed")
        val characterId = applicationManager.uiModel.value.state.character?.characterId ?: return

        scope?.launch(dispatcherProvider.ioDispatcher()) {
            val killList = serviceClient.getKillList(
                character_id = characterId,
                namespace = Namespace.PS2PC,
                CensusLang.EN,
            ).body ?: emptyList()
            _uiState.value = _uiState.value.copy(
                killList = killList.toImmutableList(),
            )
        }
    }

    /**
     * Select a [character] to be selected as the observed [Character] globally within the application.
     */
    fun selectCharacter(character: Character) {
        applicationManager.setCharacter(character.characterId)
    }

    /**
     * Open the settings screen.
     */
    fun openSettings() {
        applicationManager.setCurrentScreen(ScreenType.SETTINGS)
    }

    fun onTrayAction() {
        applicationManager.onTrayAction()
    }

    companion object {
        const val TAG = "MainViewModel"
    }
}
