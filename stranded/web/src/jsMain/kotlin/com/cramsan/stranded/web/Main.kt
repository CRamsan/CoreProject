package com.cramsan.stranded.web

import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import com.cramsan.stranded.lib.game.intent.Consume
import com.cramsan.stranded.lib.game.intent.Craft
import com.cramsan.stranded.lib.game.intent.EndTurn
import com.cramsan.stranded.lib.game.intent.Forage
import com.cramsan.stranded.lib.game.intent.SelectCard
import com.cramsan.stranded.lib.game.intent.Transfer
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
import com.cramsan.stranded.server.CommonClient
import com.cramsan.stranded.server.game.PlayerIntent
import com.cramsan.stranded.server.game.StateChange
import com.cramsan.stranded.testgui.ConnectionScreen
import com.cramsan.stranded.testgui.ConnectionViewModel
import com.cramsan.stranded.testgui.game.GameScreen
import com.cramsan.stranded.testgui.game.GameViewModel
import com.cramsan.stranded.web.game.GameScreen
import com.cramsan.stranded.web.game.GameViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.renderComposable

fun main() {


    /**
     * Instantiate client.
     */
    /*
    val client = CommonClient(
        json(),
        Dispatchers.Main,
    )

    val roomId = URLSearchParams(window.location.href).get("roomId")

    if (roomId.isNullOrBlank()) {
        lobbyScreen(client)
    } else {
        gameScreen(client)
    }
     */
    debugGameScreen()

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
        val phase = gameViewModel.phase.collectAsState()
        val day = gameViewModel.day.collectAsState()

        Div {
            GameScreen(
                name = name.value,
                health = health.value,
                phase = phase.value,
                day = day.value,
                viewModel = gameViewModel,
            )
        }
    }
}

fun debugGameScreen() {
    renderComposable(rootElementId = "root") {
        Div {
            GameScreen(
                name = "cramsan",
                health = 3,
                phase = Phase.NIGHT,
                day = 2,
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