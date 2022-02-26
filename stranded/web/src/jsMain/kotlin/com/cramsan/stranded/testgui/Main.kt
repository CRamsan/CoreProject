package com.cramsan.stranded.testgui.com.cramsan.stranded.testgui

import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import com.cramsan.stranded.lib.game.intent.Consume
import com.cramsan.stranded.lib.game.intent.Craft
import com.cramsan.stranded.lib.game.intent.EndTurn
import com.cramsan.stranded.lib.game.intent.Forage
import com.cramsan.stranded.lib.game.intent.SelectCard
import com.cramsan.stranded.lib.game.intent.Transfer
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
import com.cramsan.stranded.server.CommonClient
import com.cramsan.stranded.server.game.PlayerIntent
import com.cramsan.stranded.server.game.StateChange
import com.cramsan.stranded.testgui.ConnectionScreen
import com.cramsan.stranded.testgui.ConnectionViewModel
import com.cramsan.stranded.testgui.game.GameScreen
import com.cramsan.stranded.testgui.game.GameViewModel
import kotlinx.browser.window
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.renderComposable
import org.w3c.dom.url.URLSearchParams

fun main() {

    val roomId = URLSearchParams(window.location.href).get("roomId")

    /**
     * Instantiate client.
     */
    val client = CommonClient(
        json(),
        Dispatchers.Main,
    )

    if (roomId.isNullOrBlank()) {
        lobbyScreen(client)
    } else {
        gameScreen(client)
    }
}

fun lobbyScreen(client: CommonClient) {
    /**
     * This is the viewModel that will power the connection UI.
     */
    val connectionViewModel = ConnectionViewModel(client)

    renderComposable(rootElementId = "root") {
        val playerName: State<String> = connectionViewModel.playerName.collectAsState()
        val lobbyName: State<String> = connectionViewModel.lobbyName.collectAsState()
        val lobbyId: State<String> = connectionViewModel.lobbyId.collectAsState()
        val playerList: State<List<String>> = connectionViewModel.playerList.collectAsState(emptyList())
        Div {
            ConnectionScreen(
                playerName = playerName.value,
                lobbyName = lobbyName.value,
                lobbyId = lobbyId.value,
                playerList = playerList.value,
                viewModel = connectionViewModel,
            )
        }
    }
}

fun gameScreen(client: CommonClient) {
    /**
     * This is the viewModel that will power the game UI.
     */
    val gameViewModel = GameViewModel(client, Dispatchers.Main)

    renderComposable(rootElementId = "root") {
        val name = gameViewModel.name.collectAsState()
        val health = gameViewModel.health.collectAsState()
        val quantity = gameViewModel.quantity.collectAsState()
        val belongings = gameViewModel.belongings.collectAsState()
        val scavengeResults = gameViewModel.scavengeResults.collectAsState()
        val craftables = gameViewModel.craftables.collectAsState()
        val phase = gameViewModel.phase.collectAsState()
        val shelters = gameViewModel.shelter.collectAsState()

        Div {
            GameScreen(
                name = name.value,
                health = health.value,
                quantity = quantity.value,
                belongings = belongings.value,
                scavengeResults = scavengeResults.value,
                craftables = craftables.value,
                phase = phase.value,
                shelters = shelters.value,
                viewModel = gameViewModel,
            )
        }
    }
}

fun json() = Json {
    prettyPrint = false
    serializersModule = SerializersModule {
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