package com.cramsan.stranded.cli.mainmenu

import com.cramsan.stranded.cli.CliScreen
import com.cramsan.stranded.cli.NavigationCommand
import com.cramsan.stranded.lib.client.Client
import com.cramsan.stranded.lib.client.controllers.DefaultMainMenuController
import com.cramsan.stranded.lib.client.controllers.MainMenuController
import com.cramsan.stranded.lib.client.controllers.MainMenuEventHandler
import com.cramsan.stranded.lib.client.ui.mainmenu.LobbyListMenu
import com.cramsan.stranded.lib.client.ui.mainmenu.LobbyMenu
import com.cramsan.stranded.lib.client.ui.mainmenu.PlayerNameMenu
import com.cramsan.stranded.lib.repository.Lobby
import com.cramsan.stranded.lib.repository.Player
import kotlinx.coroutines.CoroutineScope

class MainMenuScreen(
    client: Client,
    scope: CoroutineScope,
) : CliScreen, PlayerNameMenu, LobbyListMenu, LobbyMenu, MainMenuEventHandler {

    private var controller: MainMenuController

    private var gameStart: NavigationCommand.GoToGame? = null

    init {
        controller = DefaultMainMenuController(
            client,
            scope,
            this,
            this,
            this,
            this,
            this,
            this,
        )
    }

    override fun startScreen() {
        controller.onShow()
    }

    override fun processInput(): NavigationCommand {
        println(availableCommandsMessage())
        var command: MainMenuCommand?
        do {
            command = readlnOrNull()?.let { commandFromString(it) }
            val arg = readArgumentIfNeeded(command)
            handleCommand(command, arg)
        }
        while (command != MainMenuCommand.EXIT && command != MainMenuCommand.START_GAME)

        return when (command) {
            MainMenuCommand.EXIT -> {
                NavigationCommand.ExitGame
            }
            MainMenuCommand.START_GAME -> {
                waitForGameStart()
            }
            else -> {
                TODO()
            }
        }
    }

    private fun waitForGameStart(): NavigationCommand {
        var counter = 0
        while (counter < 60) {
            Thread.sleep(1000)
            counter++
            gameStart?.let {
                return it
            }
        }
        return NavigationCommand.ExitGame
    }

    private fun handleCommand(command: MainMenuCommand?, arg: String?) {
        when (command) {
            MainMenuCommand.SET_NAME -> {
                controller.openPlayerNameMenu()
                controller.onPlayerNameConfirmed(requireNotNull(arg))
            }
            MainMenuCommand.LIST -> {
                controller.openLobbyListMenu()
            }
            MainMenuCommand.JOIN -> {
                controller.onLobbySelected(requireNotNull(arg))
            }
            MainMenuCommand.LEAVE -> {
                controller.onLeaveLobbySelected()
            }
            MainMenuCommand.CREATE -> {
                controller.openCreateLobbyMenu()
                controller.onCreateLobbySelected(requireNotNull(arg))
            }
            MainMenuCommand.SET_READY -> {
                controller.onReadySelected(true)
            }
            MainMenuCommand.SET_NOT_READY -> {
                controller.onReadySelected(false)
            }
            MainMenuCommand.START_GAME -> {
                controller.onStartGameSelected()
            }
            MainMenuCommand.HELP -> println(com.cramsan.stranded.cli.gamemenu.availableCommandsMessage())
            MainMenuCommand.EXIT -> TODO()
            null -> Unit
        }
    }

    private fun readArgumentIfNeeded(command: MainMenuCommand?): String? = when (command) {
        MainMenuCommand.SET_NAME, MainMenuCommand.JOIN, MainMenuCommand.CREATE -> readln()
        MainMenuCommand.LIST, MainMenuCommand.LEAVE, MainMenuCommand.SET_READY, MainMenuCommand.SET_NOT_READY,
        MainMenuCommand.START_GAME, MainMenuCommand.HELP, MainMenuCommand.EXIT, null -> null
    }

    override fun stopScreen() {
        controller.onDispose()
    }

    override fun setPlayerName(playerName: String) {
        println("Player name set to $playerName")
    }

    override fun setLobbyList(lobbyList: List<Lobby>) {
        println("Lobbies available:")
        lobbyList.forEach {
            println(it)
        }
    }

    override fun updatePlayer(player: Player) {
        println("Player updated to $player")
    }

    override fun setPlayerList(playerList: List<Player>) {
        println("Players in lobby:")
        playerList.forEach {
            println(it)
        }
    }

    override fun addPlayer(player: Player) {
        println("Player ${player.name} joined the lobby")
    }

    override fun removePlayer(player: Player) {
        println("Player ${player.name} left the lobby")
    }

    override fun setVisible(isVisible: Boolean) = Unit

    override fun onExitSelected() {
        TODO("Not yet implemented")
    }

    override fun onGameStarted(playerId: String, playerList: List<Player>, lobbyId: String) {
        gameStart = NavigationCommand.GoToGame(playerId, playerList, lobbyId)
    }

    override fun onDebugGameScreenSelected() {
        TODO("Not yet implemented")
    }
}