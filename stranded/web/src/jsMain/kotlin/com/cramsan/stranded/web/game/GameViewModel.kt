package com.cramsan.stranded.web.game

import com.cramsan.stranded.lib.game.intent.Consume
import com.cramsan.stranded.lib.game.intent.EndTurn
import com.cramsan.stranded.lib.game.logic.Game
import com.cramsan.stranded.lib.game.logic.MutableStrandedGameState
import com.cramsan.stranded.lib.game.logic.StrandedGameState
import com.cramsan.stranded.lib.game.models.common.Belongings
import com.cramsan.stranded.lib.game.models.common.Phase
import com.cramsan.stranded.lib.game.models.crafting.Craftable
import com.cramsan.stranded.lib.game.models.crafting.Shelter
import com.cramsan.stranded.lib.game.models.scavenge.ScavengeResult
import com.cramsan.stranded.lib.game.models.state.StrandedStateChange
import com.cramsan.stranded.server.CommonClient
import com.cramsan.stranded.server.game.ClientEventHandler
import com.cramsan.stranded.server.messages.Connected
import com.cramsan.stranded.server.messages.Disconnected
import com.cramsan.stranded.server.messages.GameChange
import com.cramsan.stranded.server.messages.GamePlayerIntent
import com.cramsan.stranded.server.messages.GameStarted
import com.cramsan.stranded.server.messages.GameStateMessage
import com.cramsan.stranded.server.messages.JoinedLobby
import com.cramsan.stranded.server.messages.LeftLobby
import com.cramsan.stranded.server.messages.ListPlayers
import com.cramsan.stranded.server.messages.LobbyCreatedFromRequest
import com.cramsan.stranded.server.messages.PlayerListFromRequest
import com.cramsan.stranded.server.messages.PlayerUpdated
import com.cramsan.stranded.server.messages.ServerEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * ViewModel that powers the UI of this demo application. This viewModel has an action for pretty much all possible
 * server and client events.
 *
 * @author cramsan
 */
class GameViewModel(
    private val client: CommonClient,
    dispatcher: CoroutineDispatcher,
) : ClientEventHandler {

    val scope = CoroutineScope(SupervisorJob() + dispatcher)

    private var playerId = ""

    private val _quantity = MutableStateFlow(0)

    private val _game = Game(
        scope,
        emptyList(),
        emptyList(),
        emptyList(),
    )

    private val _gameState = MutableStateFlow(_game.gameState)

    val phase = MutableStateFlow(Phase.NIGHT)
    val name = MutableStateFlow("")
    val health = MutableStateFlow(0)
    val belongings = MutableStateFlow<List<Belongings>>(emptyList())
    val scavengeResults = MutableStateFlow<List<ScavengeResult>>(emptyList())
    val craftables = MutableStateFlow<List<Craftable>>(emptyList())
    val shelter = MutableStateFlow<List<Shelter>>(emptyList())
    val day = MutableStateFlow(0)

    init {
        client.registerListener(this)
    }

    override fun onServerEventReceived(serverEvent: ServerEvent) {
        when (serverEvent) {
            is Connected -> playerId = serverEvent.playerId
            Disconnected -> playerId = ""
            GameStarted, is JoinedLobby, is LeftLobby,
            is LobbyCreatedFromRequest, is PlayerUpdated -> Unit
            is PlayerListFromRequest -> Unit
            is GameChange -> {
                _game.processEvent(serverEvent.change as StrandedStateChange)
                applyState(_gameState.value)
            }
            is GameStateMessage -> {
                _game.setGameState(serverEvent.gameState as StrandedGameState)
                applyState(_gameState.value)
            }
        }
    }

    /**
     * Self-explanatory
     */
    fun onListPlayersSelected() {
        client.sendMessage(ListPlayers)
    }

    fun onCardSelected(id: String) {
        client.sendMessage(GamePlayerIntent(Consume(id)))
    }

    fun onEndTurnSelected() {
        client.sendMessage(GamePlayerIntent(EndTurn))
    }

    fun onForageSelected() {
        // client.sendMessage(GamePlayerIntent(Forage(quantity.value)))
    }

    fun onCardQuantityUpdated(it: String) {
        _quantity.value = it.toIntOrNull() ?: 0
    }

    private fun applyState(gameState: StrandedGameState) {
        phase.value = gameState.phase
        val player = gameState.gamePlayers.find { it.id == playerId }
        name.value = player?.nane ?: "unknown"
        health.value = player?.health ?: -1
        belongings.value = player?.belongings ?: emptyList()
        scavengeResults.value = player?.scavengeResults ?: emptyList()
        craftables.value = player?.craftables ?: emptyList()
        shelter.value = gameState.shelters
        day.value = gameState.night
    }

    companion object {
        private val EMPTY_GAME_STATE = MutableStrandedGameState(
            mutableListOf(),
            mutableListOf(),
            mutableListOf(),
            mutableListOf(),
        )
    }
}
