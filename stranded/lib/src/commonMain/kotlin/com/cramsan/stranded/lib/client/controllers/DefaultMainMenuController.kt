package com.cramsan.stranded.lib.client.controllers

import com.cramsan.stranded.lib.client.Client
import com.cramsan.stranded.lib.client.UIComponent
import com.cramsan.stranded.lib.client.ui.mainmenu.LobbyListMenu
import com.cramsan.stranded.lib.client.ui.mainmenu.LobbyMenu
import com.cramsan.stranded.lib.client.ui.mainmenu.PlayerNameMenu
import com.cramsan.stranded.lib.messages.Connected
import com.cramsan.stranded.lib.messages.CreateLobby
import com.cramsan.stranded.lib.messages.Disconnected
import com.cramsan.stranded.lib.messages.GameChange
import com.cramsan.stranded.lib.messages.GameStarted
import com.cramsan.stranded.lib.messages.GameStateMessage
import com.cramsan.stranded.lib.messages.Ignore
import com.cramsan.stranded.lib.messages.JoinLobby
import com.cramsan.stranded.lib.messages.JoinedLobby
import com.cramsan.stranded.lib.messages.LeaveLobby
import com.cramsan.stranded.lib.messages.LeftLobby
import com.cramsan.stranded.lib.messages.ListLobbies
import com.cramsan.stranded.lib.messages.ListPlayers
import com.cramsan.stranded.lib.messages.LobbyCreated
import com.cramsan.stranded.lib.messages.LobbyCreatedFromRequest
import com.cramsan.stranded.lib.messages.LobbyDestroyed
import com.cramsan.stranded.lib.messages.LobbyList
import com.cramsan.stranded.lib.messages.PlayerListFromRequest
import com.cramsan.stranded.lib.messages.PlayerUpdated
import com.cramsan.stranded.lib.messages.ServerEvent
import com.cramsan.stranded.lib.messages.SetPlayerName
import com.cramsan.stranded.lib.messages.SetReadyToStart
import com.cramsan.stranded.lib.messages.StartGame
import com.cramsan.stranded.lib.repository.Player
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DefaultMainMenuController(
    val client: Client,
    private val mainScope: CoroutineScope,
    private var playerNameMenu: PlayerNameMenu,
    private var mainMenu: UIComponent,
    private var createLobbyMenu: UIComponent,
    private var lobbyListMenu: LobbyListMenu,
    private var lobbyMenu: LobbyMenu,
    private var mainMenuEventHandler: MainMenuEventHandler,
) :
    MainMenuController {

    private var lobbyId: String = ""
    private var playerList: MutableList<Player> = mutableListOf()

    private var mode: MainMenuMode = MainMenuMode.None
        set(value) {
            setMenuMode(value)
            field = value
        }

    override fun onShow() {
        client.registerListener(this)
        mode = MainMenuMode.None
    }

    override fun onDispose() {
        client.deregisterListener(this)
    }

    override fun openPlayerNameMenu() {
        mode = MainMenuMode.PlayerName
        if (client.player.name.isNotBlank()) {
            playerNameMenu.setPlayerName(client.player.name)
        }
    }

    override fun closeApplication() {
        mainMenuEventHandler.onExitSelected()
    }

    override fun onServerEventReceived(serverEvent: ServerEvent) {
        mainScope.launch {
            when (serverEvent) {
                is Connected -> {
                    mode = MainMenuMode.PlayerName
                }
                Disconnected -> {
                    mode = MainMenuMode.None
                }
                Ignore -> Unit
                is JoinedLobby -> {
                    handleJoinedLobby(serverEvent)
                }
                is LeftLobby -> {
                    handleLeftLobby(serverEvent)
                }
                is LobbyCreated -> Unit
                is LobbyDestroyed -> Unit
                is LobbyList -> lobbyListMenu.setLobbyList(serverEvent.lobbyList)
                is PlayerUpdated -> handlePlayerUpdate(serverEvent.player)
                is LobbyCreatedFromRequest -> {
                    client.sendMessage(JoinLobby(serverEvent.lobby.id))
                }
                is PlayerListFromRequest -> {
                    playerList = serverEvent.playerList.toMutableList()
                    handlePlayerListUpdate()
                }
                GameStarted -> mainMenuEventHandler.onGameStarted(client.player.id, playerList, lobbyId)
                is GameChange -> Unit
                is GameStateMessage -> Unit
            }
        }
    }

    private fun handleLeftLobby(serverEvent: LeftLobby) {
        when (mode) {
            MainMenuMode.CreateLobby, MainMenuMode.LobbyList, MainMenuMode.PlayerName,
            MainMenuMode.MainMenu, MainMenuMode.None -> Unit
            MainMenuMode.Lobby -> {
                require(serverEvent.lobbyId == lobbyId)
                if (serverEvent.player.id == client.player.id) {
                    mode = MainMenuMode.MainMenu
                } else {
                    lobbyMenu.removePlayer(serverEvent.player)
                }
            }
        }
    }

    private fun handleJoinedLobby(serverEvent: JoinedLobby) {
        when (mode) {
            MainMenuMode.CreateLobby, MainMenuMode.LobbyList -> {
                require(serverEvent.player.id == client.player.id)
                mode = MainMenuMode.Lobby
                lobbyId = serverEvent.lobbyId
                client.sendMessage(ListPlayers)
            }
            MainMenuMode.PlayerName -> Unit
            MainMenuMode.MainMenu -> Unit
            MainMenuMode.Lobby -> {
                lobbyMenu.addPlayer(serverEvent.player)
            }
            MainMenuMode.None -> Unit
        }
    }

    private fun handlePlayerListUpdate() {
        lobbyMenu.setPlayerList(playerList)
    }

    private fun handlePlayerUpdate(player: Player) {
        when (mode) {
            MainMenuMode.PlayerName -> {
                if (player.id == client.player.id) {
                    mode = MainMenuMode.MainMenu
                }
            }
            MainMenuMode.Lobby -> {
                lobbyMenu.updatePlayer(player)
            }
            MainMenuMode.MainMenu, MainMenuMode.CreateLobby,
            MainMenuMode.LobbyList, MainMenuMode.None -> Unit
        }
    }

    private fun setMenuMode(mode: MainMenuMode) {
        playerNameMenu.setVisible(mode == MainMenuMode.PlayerName)
        mainMenu.setVisible(mode == MainMenuMode.MainMenu)
        createLobbyMenu.setVisible(mode == MainMenuMode.CreateLobby)
        lobbyListMenu.setVisible(mode == MainMenuMode.LobbyList)
        lobbyMenu.setVisible(mode == MainMenuMode.Lobby)
    }

    override fun openCreateLobbyMenu() {
        mode = MainMenuMode.CreateLobby
    }

    override fun openLobbyListMenu() {
        mode = MainMenuMode.LobbyList
        client.sendMessage(ListLobbies)
    }

    override fun onReturnToMainMenuSelected() {
        mode = MainMenuMode.MainMenu
    }

    override fun onCreateLobbySelected(lobbyName: String) {
        client.sendMessage(CreateLobby(lobbyName))
    }

    override fun onLeaveLobbySelected() {
        client.sendMessage(LeaveLobby)
    }

    override fun onReadySelected(isReady: Boolean) {
        client.sendMessage(SetReadyToStart(isReady))
    }

    override fun onStartGameSelected() {
        client.sendMessage(StartGame)
    }

    override fun onLobbySelected(lobbyId: String) {
        client.sendMessage(JoinLobby(lobbyId))
    }

    override fun onPlayerNameConfirmed(playerName: String) {
        client.sendMessage(SetPlayerName(playerName))
    }

    override fun onDebugScreenSelected() {
        mainMenuEventHandler.onDebugGameScreenSelected()
    }
}
