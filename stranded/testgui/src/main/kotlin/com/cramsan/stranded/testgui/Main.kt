package com.cramsan.stranded.testgui

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.cramsan.stranded.lib.game.intent.Consume
import com.cramsan.stranded.lib.game.intent.Craft
import com.cramsan.stranded.lib.game.intent.EndTurn
import com.cramsan.stranded.lib.game.intent.Forage
import com.cramsan.stranded.lib.game.intent.SelectCard
import com.cramsan.stranded.lib.game.intent.Transfer
import com.cramsan.stranded.lib.game.logic.MutableStrandedGameState
import com.cramsan.stranded.lib.game.models.common.Phase
import com.cramsan.stranded.lib.game.models.state.CraftCard
import com.cramsan.stranded.lib.game.models.state.DestroyShelter
import com.cramsan.stranded.lib.game.models.state.DrawBelongingCard
import com.cramsan.stranded.lib.game.models.state.DrawNightCard
import com.cramsan.stranded.lib.game.models.state.DrawScavengeCard
import com.cramsan.stranded.lib.game.models.state.ExtinguishFire
import com.cramsan.stranded.lib.game.models.state.IncrementNight
import com.cramsan.stranded.lib.game.models.state.LoseCard
import com.cramsan.stranded.lib.game.models.state.SetFireBlockStatus
import com.cramsan.stranded.lib.game.models.state.SetPhase
import com.cramsan.stranded.lib.game.models.state.SingleHealthChange
import com.cramsan.stranded.lib.game.models.state.UserCard
import com.cramsan.stranded.lib.messages.module
import com.cramsan.stranded.lib.storage.FileBasedCardRepository
import com.cramsan.stranded.server.JvmClient
import com.cramsan.stranded.server.Server
import com.cramsan.stranded.server.game.GameState
import com.cramsan.stranded.server.game.PlayerIntent
import com.cramsan.stranded.server.game.StateChange
import com.cramsan.stranded.server.repository.ConnectionRepository
import com.cramsan.stranded.server.repository.GameRepository
import com.cramsan.stranded.server.repository.LobbyRepository
import com.cramsan.stranded.server.repository.PlayerRepository
import com.cramsan.stranded.testgui.game.GameFactory
import com.cramsan.stranded.testgui.game.GameScreen
import com.cramsan.stranded.testgui.game.GameViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
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
        prettyPrint = false
        serializersModule = SerializersModule {
            polymorphic(GameState::class) {
                subclass(MutableStrandedGameState::class)
            }
            polymorphic(PlayerIntent::class) {
                subclass(Forage::class)
                subclass(Consume::class)
                subclass(Transfer::class)
                subclass(Craft::class)
                subclass(SelectCard::class)
                subclass(EndTurn::class)
            }
            polymorphic(StateChange::class) {
                subclass(SingleHealthChange::class)
                subclass(DrawBelongingCard::class)
                subclass(DrawScavengeCard::class)
                subclass(DrawNightCard::class)
                subclass(IncrementNight::class)
                subclass(SetPhase::class)
                subclass(UserCard::class)
                subclass(CraftCard::class)
                subclass(ExtinguishFire::class)
                subclass(SetFireBlockStatus::class)
                subclass(DestroyShelter::class)
                subclass(LoseCard::class)
            }
        }
    }

    val cardRepository = FileBasedCardRepository(
        filename = "test.json",
        json = Json {
            serializersModule = module
            prettyPrint = true
        }
    )
    cardRepository.initialize()

    val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    /**
     * Initialize server dependencies.
     */
    val demoGameRepository = GameFactory(scope, cardRepository)
    val playerRepository = PlayerRepository()
    val lobbyRepository = LobbyRepository(playerRepository)
    val gameRepository = GameRepository(demoGameRepository)
    val connectionRepository = ConnectionRepository()

    /**
     * Instantiate a server and client.
     */
    val client = JvmClient(
        json,
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
     * This is the viewModel that will power the connection UI.
     */
    val connectionViewModel = ConnectionViewModel(client, server, Dispatchers.Main)

    val playerName: State<String> = connectionViewModel.playerName.collectAsState()
    val lobbyName: State<String> = connectionViewModel.lobbyName.collectAsState()
    val lobbyId: State<String> = connectionViewModel.lobbyId.collectAsState()
    val gameContent: State<String> = connectionViewModel.gameContent.collectAsState()
    val playerListContent: State<String> = connectionViewModel.playerListContent.collectAsState("")

    /**
     * This is the viewModel that will power the game UI.
     */
    val gameViewModel = GameViewModel(client, Dispatchers.Main)

    val name = gameViewModel.name.collectAsState()
    val health = gameViewModel.health.collectAsState()
    val quantity = gameViewModel.quantity.collectAsState()
    val belongings = gameViewModel.belongings.collectAsState()
    val scavengeResults = gameViewModel.scavengeResults.collectAsState()
    val craftables = gameViewModel.craftables.collectAsState()
    val phase = gameViewModel.phase.collectAsState()
    val shelter = gameViewModel.shelter.collectAsState()

    Window(onCloseRequest = ::exitApplication) {
        MaterialTheme {
            ConnectionScreen(
                playerName.value,
                lobbyName.value,
                lobbyId.value,
                gameContent.value,
                playerListContent.value,
                connectionViewModel,
            )
        }
    }

    Window(onCloseRequest = ::exitApplication) {
        MaterialTheme {
            GameScreen(
                name.value,
                health.value,
                quantity.value,
                belongings.value,
                scavengeResults.value,
                craftables.value,
                phase.value,
                shelter.value,
                gameViewModel,
            )
        }
    }
}
