package com.cramsan.stranded.lib.game.logic

import com.cramsan.stranded.lib.game.intent.Consume
import com.cramsan.stranded.lib.game.intent.Craft
import com.cramsan.stranded.lib.game.intent.EndTurn
import com.cramsan.stranded.lib.game.intent.Forage
import com.cramsan.stranded.lib.game.intent.PlayerIntent
import com.cramsan.stranded.lib.game.intent.Transfer
import com.cramsan.stranded.lib.game.models.GamePlayer
import com.cramsan.stranded.lib.game.models.common.Belongings
import com.cramsan.stranded.lib.game.models.common.Phase
import com.cramsan.stranded.lib.game.models.night.NightEvent
import com.cramsan.stranded.lib.game.models.scavenge.ScavengeResult
import com.cramsan.stranded.lib.game.models.state.CancellableByFire
import com.cramsan.stranded.lib.game.models.state.Change
import com.cramsan.stranded.lib.game.models.state.CraftCard
import com.cramsan.stranded.lib.game.models.state.DrawBelongingCard
import com.cramsan.stranded.lib.game.models.state.DrawNightCard
import com.cramsan.stranded.lib.game.models.state.DrawScavengeCard
import com.cramsan.stranded.lib.game.models.state.IncrementNight
import com.cramsan.stranded.lib.game.models.state.SetPhase
import com.cramsan.stranded.lib.game.models.state.SingleHealthChange
import com.cramsan.stranded.lib.game.models.state.UserCard
import com.cramsan.stranded.lib.repository.Player
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

/**
 * This class is where the logic for the game resides.
 */
class Game(
    private val gameScope: CoroutineScope,
) {
    private lateinit var _mutableGameState: MutableGameState

    val gameState: GameState
        get() { return _mutableGameState }

    lateinit var playerIntents: Map<String, Channel<PlayerIntent>>

    var gameEventHandler: GameEventHandler? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }

    fun setGameState(gameState: GameState) {
        _mutableGameState = MutableGameState(
            gameState.gamePlayers,
            gameState.scavengeStack.toMutableList(),
            gameState.nightStack.toMutableList(),
            gameState.belongingsStack.toMutableList(),
            gameState.shelters.toMutableList(),
            gameState.hasFire,
            gameState.isFireBlocked,
            gameState.night,
            gameState.targetList,
            gameState.fireDamageMod,
            gameState.phase,
        )
    }

    fun configureGame(
        players: List<Player>,
        scavengeStack: List<ScavengeResult>,
        nightStack: List<NightEvent>,
        belongingsStack: List<Belongings>,
    ) {
        _mutableGameState = MutableGameState(
            players.map {
                GamePlayer(
                    it.id,
                    it.name,
                    4,
                )
            },
            scavengeStack.toMutableList(),
            nightStack.toMutableList(),
            belongingsStack.toMutableList(),
        )
        playerIntents = _mutableGameState.gamePlayers.associate {
            it.id to Channel(
                capacity = Channel.UNLIMITED,
            )
        }
    }

    internal suspend fun startGameJobAsync() = gameScope.launch(exceptionHandler) {
        dealInitialHand()

        while (_mutableGameState.nightStack.isNotEmpty()) {
            performNightPhase()
            performDayPhase()
        }

        println("Game is over")
    }

    private fun dealInitialHand() {
        _mutableGameState.gamePlayers.forEach {
            println("Player: ${it.id}: Dealing initial card")
            processEvent(DrawBelongingCard(it.id))
        }
    }

    private suspend fun performNightPhase() {
        processEvent(SetPhase(Phase.NIGHT))
        println("Starting night: ${_mutableGameState.night}")
        val nightEvent = _mutableGameState.nightStack.last()
        processEvent(DrawNightCard)

        println("Got Night Event: $nightEvent")
        processNightEvent(nightEvent)
        processEvent(IncrementNight)
    }

    suspend fun processNightEvent(nightEvent: NightEvent) {
        nightEvent.statements.sortedBy { it.priority }.forEach {
            when (it) {
                CancellableByFire -> {
                    if (_mutableGameState.hasFire) return
                }
                else -> {
                    processEvent(it)
                }
            }
        }

        _mutableGameState.gamePlayers.map {
            gameScope.launch(exceptionHandler) {
                val eventChannel = playerIntents.getValue(it.id)

                while (true) {
                    val playerEvent = eventChannel.receive()
                    if (playerEvent is EndTurn)
                        break
                }
            }
        }.joinAll()
    }

    private suspend fun performDayPhase() {
        println("Starting forage phase")
        processEvent(SetPhase(Phase.FORAGING))
        _mutableGameState.gamePlayers.map {
            startPlayerTurnForaging(it)
        }.joinAll()
        println("Starting night-prepare phase")
        processEvent(SetPhase(Phase.NIGHT_PREPARE))
        _mutableGameState.gamePlayers.map {
            startPlayerTurnPreparingForNight(it)
        }.joinAll()
    }

    private fun startPlayerTurnForaging(player: GamePlayer) = gameScope.launch(exceptionHandler) {
        if (player.health <= 0) {
            println("Player: ${player.id}: Player is dead")
            return@launch
        }

        println("Player: ${player.id}: Starting to forage")

        val eventChannel = playerIntents.getValue(player.id)

        var playerEvent: PlayerIntent

        do {
            println("Player: ${player.id}: Waiting for payment")
            playerEvent = eventChannel.receive()
            println("Player: ${player.id}: Payment received")
        } while (playerEvent !is Forage)

        processEvent(SingleHealthChange(player.id, -playerEvent.amount))
        println("Player: ${player.id}: Player paid ${playerEvent.amount}")

        (0 until playerEvent.amount).forEach {
            processEvent(DrawScavengeCard(player.id))
        }
    }

    private fun startPlayerTurnPreparingForNight(player: GamePlayer) = gameScope.launch(exceptionHandler) {
        println("Player: ${player.id}: Preparing for night")

        val eventChannel = playerIntents.getValue(player.id)

        var playerEvent: PlayerIntent

        do {
            println("Player: ${player.id}: Waiting for action")
            playerEvent = eventChannel.receive()
            println("Player: ${player.id}: Action received")

            handleEvent(player, playerEvent)
        } while (playerEvent !is EndTurn)

        println("Player: ${player.id}: Turn is over")
    }

    private fun handleEvent(player: GamePlayer, playerIntent: PlayerIntent) {
        println("Player: ${player.id}: Handling action $playerIntent")
        when (playerIntent) {
            is EndTurn, is Forage -> return
            is Transfer -> TODO()
            is Consume -> {
                processEvent(UserCard(player.id, playerIntent.cardId))
            }
            is Craft -> {
                processEvent(CraftCard(player.id, playerIntent.targetList, playerIntent.craftable))
            }
        }
    }

    fun processEvent(change: Change) {
        _mutableGameState.processEvent(change, gameEventHandler)
    }
}
