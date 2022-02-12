package com.cramsan.stranded.lib.client.controllers

import com.cramsan.stranded.lib.client.UIComponent
import com.cramsan.stranded.lib.client.ui.mainmenu.LobbyListMenu
import com.cramsan.stranded.lib.client.ui.mainmenu.LobbyMenu
import com.cramsan.stranded.lib.client.ui.mainmenu.PlayerNameMenu
import com.cramsan.stranded.server.Client
import com.cramsan.stranded.server.messages.Connected
import com.cramsan.stranded.server.messages.CreateLobby
import com.cramsan.stranded.server.messages.Disconnected
import com.cramsan.stranded.server.messages.GameChange
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
            field = value
            handleMenuModeChange(value)
        }

    override fun onShow() {
        if (!client.isConnected()) {
            client.registerListener(this)
            mode = MainMenuMode.None
        } else {
            mode = MainMenuMode.MainMenu
        }
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
                is JoinedLobby -> {
                    handleJoinedLobby(serverEvent)
                }
                is LeftLobby -> {
                    handleLeftLobby(serverEvent)
                }
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

    private fun handleMenuModeChange(mode: MainMenuMode) {
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
        // TODO: Remove this function since we do not list lobbies anymore
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
        client.sendMessage(SetReadyToStartGame(isReady))
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
