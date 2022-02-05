package com.cramsan.stranded.cli


import com.cramsan.stranded.cli.gamemenu.GameScreen
import com.cramsan.stranded.cli.mainmenu.MainMenuScreen
import com.cramsan.stranded.lib.JvmClient
import com.cramsan.stranded.lib.Server
import com.cramsan.stranded.lib.client.Client
import com.cramsan.stranded.lib.repository.Player
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

fun main() {
    println("Initializing configuration")
    /*
    val cardRepository = FileBasedCardRepository(
        filename = "test.json",
        json = Json {
            serializersModule = module
        }
    )
     */

    val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    val server = Server()
    val client = JvmClient()

    server.start()
    client.start()

    var navigationCommand: NavigationCommand = NavigationCommand.GoToMainMenu
    while (navigationCommand != NavigationCommand.ExitGame) {
        val currentScreen: CliScreen = when (navigationCommand) {
            is NavigationCommand.GoToGame -> {
                val playerId = navigationCommand.playerId
                val playerList = navigationCommand.playerList
                val lobbyId = navigationCommand.lobbyId
                createGame(
                    playerId,
                    playerList,
                    lobbyId,
                    client,
                    scope
                )
            }
            NavigationCommand.GoToMainMenu -> createMainMenu(client, scope)
            NavigationCommand.ExitGame -> TODO()
        }

        navigationCommand = runScreen(currentScreen)
    }
}

fun runScreen(cliScreen: CliScreen): NavigationCommand {
    cliScreen.startScreen()
    val navigation = cliScreen.processInput()
    cliScreen.stopScreen()
    return navigation
}

fun createMainMenu(
    client: Client,
    scope: CoroutineScope,
) = MainMenuScreen (
    client,
    scope,
)

fun createGame(
    playerId: String,
    playerList: List<Player>,
    lobbyId: String,
    client: Client,
    scope: CoroutineScope,
) = GameScreen (
    playerId,
    playerList,
    lobbyId,
    client,
    scope,
)
