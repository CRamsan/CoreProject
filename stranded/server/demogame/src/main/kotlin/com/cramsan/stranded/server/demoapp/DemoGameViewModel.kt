package com.cramsan.stranded.server.demoapp

import com.cramsan.stranded.server.Client
import com.cramsan.stranded.server.Server
import com.cramsan.stranded.server.demoapp.game.DemoGameState
import com.cramsan.stranded.server.demoapp.game.DemoGameStateChange
import com.cramsan.stranded.server.demoapp.game.RequestIncrementCounter
import com.cramsan.stranded.server.demoapp.game.transformWithStateChange
import com.cramsan.stranded.server.game.ClientEventHandler
import com.cramsan.stranded.server.messages.Connected
import com.cramsan.stranded.server.messages.CreateLobby
import com.cramsan.stranded.server.messages.DeleteLobby
import com.cramsan.stranded.server.messages.Disconnected
import com.cramsan.stranded.server.messages.GameChange
import com.cramsan.stranded.server.messages.GamePlayerIntent
import com.cramsan.stranded.server.messages.GameStarted
import com.cramsan.stranded.server.messages.GameStateMessage
import com.cramsan.stranded.server.messages.JoinLobby
import com.cramsan.stranded.server.messages.JoinedLobby
import com.cramsan.stranded.server.messages.LeaveLobby
import com.cramsan.stranded.server.messages.LeftLobby
import com.cramsan.stranded.server.messages.ListPlayers
import com.cramsan.stranded.server.messages.LobbyCreatedFromRequest
import com.cramsan.stranded.server.messages.PlayerListFromRequest
import com.cramsan.stranded.server.messages.PlayerUpdated
import com.cramsan.stranded.server.messages.ServerEvent
import com.cramsan.stranded.server.messages.SetPlayerName
import com.cramsan.stranded.server.messages.SetReadyToStartGame
import com.cramsan.stranded.server.messages.StartGame
import com.cramsan.stranded.server.repository.Player
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * ViewModel that powers the UI of this demo application. This viewModel has an action for pretty much all possible
 * server and client events.
 *
 * @author cramsan
 */
class DemoGameViewModel(
    private val client: Client,
    private val server: Server,
    private val json: Json,
    dispatcher: CoroutineDispatcher,
) : ClientEventHandler {

    private var scope = CoroutineScope(SupervisorJob() + dispatcher)

    init {
        client.registerListener(this)
    }

    private var playerId = ""
    private var _gameState: DemoGameState? = null

    private val _playerName = MutableStateFlow("")
    private val _lobbyName = MutableStateFlow("")
    private val _lobbyId = MutableStateFlow("")
    private val _gameContent = MutableStateFlow("")
    private val _playerList = MutableStateFlow<List<Player>>(emptyList())
    private val _playerListContent: Flow<String> = _playerList.map { players ->
        players.joinToString("\n") {
            "${it.name} - ${it.readyToStart}"
        }
    }

    /**
     * Observable flow that contains the player name.
     */
    val playerName: StateFlow<String> = _playerName

    /**
     * Observable flow that contains the lobby name.
     */
    val lobbyName: StateFlow<String> = _lobbyName

    /**
     * Observable flow that contains the lobby Id.
     */
    val lobbyId: StateFlow<String> = _lobbyId

    /**
     * Observable flow that contains the state of the game.
     */
    val gameContent: StateFlow<String> = _gameContent

    val playerListContent: Flow<String> = _playerListContent

    /**
     * Called when the current user modified the name field to be [playerName].
     */
    fun onPlayerNameUpdated(playerName: String) {
        _playerName.value = playerName
    }

    /**
     * Called when the current user modified the lobby name field to be [lobbyName].
     */
    fun onLobbyNameUpdated(lobbyName: String) {
        _lobbyName.value = lobbyName
    }

    /**
     * Called when the current user modified the lobby id field to be [lobbyId].
     */
    fun onLobbyIdUpdated(lobbyId: String) {
        _lobbyId.value = lobbyId
    }

    /**
     * Self-explanatory
     */
    fun onGameActionSelected() {
        client.sendMessage(GamePlayerIntent(RequestIncrementCounter))
    }

    /**
     * Self-explanatory
     */
    fun onStartGameSelected() {
        client.sendMessage(StartGame)
    }

    /**
     * Self-explanatory
     */
    fun onSetNotReadySelected() {
        client.sendMessage(SetReadyToStartGame(false))
    }

    /**
     * Self-explanatory
     */
    fun onSetReadySelected() {
        client.sendMessage(SetReadyToStartGame(true))
    }

    /**
     * Self-explanatory
     */
    fun onDeleteLobbySelected() {
        client.sendMessage(DeleteLobby)
    }

    /**
     * Self-explanatory
     */
    fun onLeaveLobbySelected() {
        client.sendMessage(LeaveLobby)
    }

    /**
     * Self-explanatory
     */
    fun onJoinLobbySelected() {
        client.sendMessage(JoinLobby(lobbyId.value))
    }

    /**
     * Self-explanatory
     */
    fun onCreateLobbySelected() {
        client.sendMessage(CreateLobby(lobbyName.value))
    }

    /**
     * Self-explanatory
     */
    fun onListPlayersSelected() {
        client.sendMessage(ListPlayers)
    }

    /**
     * Self-explanatory
     */
    fun onStartServerSelected() {
        server.start()
    }

    /**
     * Self-explanatory
     */
    fun onStopServerSelected() {
        server.stop()
    }

    /**
     * Self-explanatory
     */
    fun onStartClientSelected() {
        client.start()
    }

    /**
     * Self-explanatory
     */
    fun onStopClientSelected() {
        client.stop()
    }

    override fun onServerEventReceived(serverEvent: ServerEvent) {
        when (serverEvent) {
            is Connected -> {
                _playerName.value = serverEvent.playerId
                playerId = serverEvent.playerId
            }
            Disconnected -> _playerName.value = ""
            is PlayerUpdated -> {
                if (serverEvent.player.id == playerId) {
                    _playerName.value = serverEvent.player.name
                }
                _playerList.value = _playerList.value.map {
                    if (it.id == serverEvent.player.id) {
                        serverEvent.player
                    } else {
                        it
                    }
                }
            }
            GameStarted -> {
                println("Game started")
            }
            is GameChange -> {
                val gameStateChange = serverEvent.change as DemoGameStateChange
                _gameState = _gameState?.transformWithStateChange(gameStateChange)
                _gameContent.value = json.encodeToString(_gameState)
            }
            is GameStateMessage -> {
                val gameState = serverEvent.gameState as DemoGameState
                _gameState = gameState
                _gameContent.value = json.encodeToString(_gameState)
            }
            is JoinedLobby -> {
                val newPlayerList = if (_playerList.value.any { it.id == serverEvent.player.id }) {
                    _playerList.value + serverEvent.player
                } else {
                    _playerList.value
                }
                _playerList.value = newPlayerList
            }
            is LeftLobby -> {
                if (serverEvent.player.id == playerId) {
                    _lobbyId.value = ""
                    _lobbyName.value = ""
                    _playerList.value = emptyList()
                } else {
                    _playerList.value = _playerList.value.filter {
                        it.id != serverEvent.player.id
                    }
                }
            }
            is LobbyCreatedFromRequest -> {
                _lobbyId.value = serverEvent.lobby.id
                _lobbyName.value = serverEvent.lobby.name
            }
            is PlayerListFromRequest -> {
                _playerList.value = serverEvent.playerList
            }
        }
    }

    fun onUpdatePlayerNameSelected() {
        client.sendMessage(SetPlayerName(_playerName.value))
    }
}
