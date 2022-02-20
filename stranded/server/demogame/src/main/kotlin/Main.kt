package com.cramsan.stranded.server.demoapp

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.cramsan.stranded.server.CommonClient
import com.cramsan.stranded.server.Server
import com.cramsan.stranded.server.demoapp.game.DemoGameState
import com.cramsan.stranded.server.demoapp.game.IncrementCounter
import com.cramsan.stranded.server.demoapp.game.RequestIncrementCounter
import com.cramsan.stranded.server.game.GameState
import com.cramsan.stranded.server.game.PlayerIntent
import com.cramsan.stranded.server.game.StateChange
import com.cramsan.stranded.server.repository.ConnectionRepository
import com.cramsan.stranded.server.repository.GameRepository
import com.cramsan.stranded.server.repository.LobbyRepository
import com.cramsan.stranded.server.repository.PlayerRepository
import io.ktor.client.engine.cio.*
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

/**
 * Application entry point.
 */
fun main() = application {

    /**
     * Json serialization used for the client and server communication.
     */
    val json = Json {
        prettyPrint = true
        serializersModule = SerializersModule {
            polymorphic(GameState::class) {
                subclass(DemoGameState::class)
            }
            polymorphic(PlayerIntent::class) {
                subclass(RequestIncrementCounter::class)
            }
            polymorphic(StateChange::class) {
                subclass(IncrementCounter::class)
            }
        }
    }

    /**
     * Initialize server dependencies.
     */
    val demoGameRepository = DemoGameFactory()
    val playerRepository = PlayerRepository()
    val lobbyRepository = LobbyRepository(playerRepository)
    val gameRepository = GameRepository(demoGameRepository)
    val connectionRepository = ConnectionRepository()

    /**
     * Instantiate a server and client.
     */
    val client = CommonClient(
        json,
        CIO,
        Dispatchers.IO,
    )
    val server = Server(
        lobbyRepository,
        playerRepository,
        gameRepository,
        connectionRepository,
        json,
        Dispatchers.IO,
    )

    /**
     * This is the viewModel that will power the UI.
     */
    val viewModel = DemoGameViewModel(client, server, json, Dispatchers.Main)

    val playerName: State<String> = viewModel.playerName.collectAsState()
    val lobbyName: State<String> = viewModel.lobbyName.collectAsState()
    val lobbyId: State<String> = viewModel.lobbyId.collectAsState()
    val gameContent: State<String> = viewModel.gameContent.collectAsState()
    val playerListContent: State<String> = viewModel.playerListContent.collectAsState("")

    Window(onCloseRequest = ::exitApplication) {
        MaterialTheme {
            DemoGameScreen(
                playerName.value,
                lobbyName.value,
                lobbyId.value,
                gameContent.value,
                playerListContent.value,
                viewModel,
            )
        }
    }
}
