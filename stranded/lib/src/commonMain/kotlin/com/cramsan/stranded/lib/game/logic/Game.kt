package com.cramsan.stranded.lib.game.logic

import com.cramsan.stranded.lib.game.intent.Consume
import com.cramsan.stranded.lib.game.intent.Craft
import com.cramsan.stranded.lib.game.intent.EndTurn
import com.cramsan.stranded.lib.game.intent.Forage
import com.cramsan.stranded.lib.game.intent.StrandedPlayerIntent
import com.cramsan.stranded.lib.game.intent.Transfer
import com.cramsan.stranded.lib.game.models.GamePlayer
import com.cramsan.stranded.lib.game.models.common.Belongings
import com.cramsan.stranded.lib.game.models.common.Phase
import com.cramsan.stranded.lib.game.models.night.CancellableByFire
import com.cramsan.stranded.lib.game.models.night.CancellableByFood
import com.cramsan.stranded.lib.game.models.night.CancellableByWeapon
import com.cramsan.stranded.lib.game.models.night.DamageToDo
import com.cramsan.stranded.lib.game.models.night.DestroyShelter
import com.cramsan.stranded.lib.game.models.night.FiberLost
import com.cramsan.stranded.lib.game.models.night.FireModification
import com.cramsan.stranded.lib.game.models.night.FireUnavailableTomorrow
import com.cramsan.stranded.lib.game.models.night.ForageCardLost
import com.cramsan.stranded.lib.game.models.night.NightEvent
import com.cramsan.stranded.lib.game.models.night.SelectTargetOnlyUnsheltered
import com.cramsan.stranded.lib.game.models.night.SelectTargetQuantity
import com.cramsan.stranded.lib.game.models.night.SelectTargetQuantityAll
import com.cramsan.stranded.lib.game.models.night.Survived
import com.cramsan.stranded.lib.game.models.scavenge.ScavengeResult
import com.cramsan.stranded.lib.game.models.state.StrandedStateChange
import com.cramsan.stranded.lib.game.models.state.CraftCard
import com.cramsan.stranded.lib.game.models.state.DrawBelongingCard
import com.cramsan.stranded.lib.game.models.state.DrawNightCard
import com.cramsan.stranded.lib.game.models.state.DrawScavengeCard
import com.cramsan.stranded.lib.game.models.state.IncrementNight
import com.cramsan.stranded.lib.game.models.state.SetPhase
import com.cramsan.stranded.lib.game.models.state.SingleHealthChange
import com.cramsan.stranded.lib.game.models.state.UserCard
import com.cramsan.stranded.server.MultiplayerGameEventHandler
import com.cramsan.stranded.server.game.MultiplayerGame
import com.cramsan.stranded.server.game.PlayerIntent
import com.cramsan.stranded.server.repository.Player
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
) : MultiplayerGame {

    private val _gameState = MutableStrandedGameState(
        mutableListOf(),
        mutableListOf(),
        mutableListOf(),
        mutableListOf(),
    )

    override val gameState: StrandedGameState = _gameState

    override fun onGameStarted() {
        TODO("Not yet implemented")
    }

    override fun onGameEnded() {
        TODO("Not yet implemented")
    }

    override fun onPlayerIntentReceived(playerId: String, playerIntent: PlayerIntent) {
        TODO("Not yet implemented")
    }

    override fun registerServerEventHandler(eventHandler: MultiplayerGameEventHandler) {
        TODO("Not yet implemented")
    }

    override fun deregisterServerEventHandler() {
        TODO("Not yet implemented")
    }

    lateinit var playerIntents: Map<String, Channel<StrandedPlayerIntent>>

    var gameEventHandler: GameEventHandler? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }

    fun setGameState(newGameState: StrandedGameState) {
        clearGameState()
        _gameState.apply {
            gamePlayers.addAll(newGameState.gamePlayers)
            scavengeStack.addAll(newGameState.scavengeStack)
            nightStack.addAll(newGameState.nightStack)
            belongingsStack.addAll(newGameState.belongingsStack)
            shelters.addAll(newGameState.shelters)
            hasFire = newGameState.hasFire
            isFireBlocked = newGameState.isFireBlocked
            night = newGameState.night
            targetList.addAll(newGameState.targetList)
            fireDamageMod = newGameState.fireDamageMod
            phase = newGameState.phase
        }
    }

    fun configureGame(
        players: List<Player>,
        scavengeStack: List<ScavengeResult>,
        nightStack: List<NightEvent>,
        belongingsStack: List<Belongings>,
    ) {
        clearGameState()
        _gameState.gamePlayers.addAll(players.map {
            GamePlayer(
                it.id,
                it.name,
                4,
            )
        })
        _gameState.scavengeStack.addAll(scavengeStack)
        _gameState.nightStack.addAll(nightStack)
        _gameState.belongingsStack.addAll(belongingsStack)
        playerIntents = gameState.gamePlayers.associate {
            it.id to Channel(
                capacity = Channel.UNLIMITED,
            )
        }
    }

    internal suspend fun startGameJobAsync() = gameScope.launch(exceptionHandler) {
        dealInitialHand()

        while (gameState.nightStack.isNotEmpty()) {
            performNightPhase()
            performDayPhase()
        }

        println("Game is over")
    }

    private fun dealInitialHand() {
        gameState.gamePlayers.forEach {
            println("Player: ${it.id}: Dealing initial card")
            processEvent(DrawBelongingCard(it.id))
        }
    }

    private suspend fun performNightPhase() {
        processEvent(SetPhase(Phase.NIGHT))
        println("Starting night: ${gameState.night}")
        val nightEvent = gameState.nightStack.last()
        processEvent(DrawNightCard)

        println("Got Night Event: $nightEvent")
        processNightEvent(nightEvent)
        processEvent(IncrementNight)
    }

    suspend fun processNightEvent(nightEvent: NightEvent) {

        nightEvent.statements.sortedBy { it.priority }.forEach {
            when (it) {
                CancellableByFire -> Unit
                is CancellableByFood -> Unit
                is CancellableByWeapon -> Unit
                is DamageToDo -> Unit
                DestroyShelter -> Unit
                FiberLost -> Unit
                is FireModification -> Unit
                FireUnavailableTomorrow -> Unit
                is ForageCardLost -> Unit
                is SelectTargetOnlyUnsheltered -> Unit
                is SelectTargetQuantity -> Unit
                SelectTargetQuantityAll -> Unit
                Survived -> Unit
            }
        }

        gameState.gamePlayers.map {
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
        gameState.gamePlayers.map {
            startPlayerTurnForaging(it)
        }.joinAll()
        println("Starting night-prepare phase")
        processEvent(SetPhase(Phase.NIGHT_PREPARE))
        gameState.gamePlayers.map {
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

        var playerEvent: StrandedPlayerIntent

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

        var playerEvent: StrandedPlayerIntent

        do {
            println("Player: ${player.id}: Waiting for action")
            playerEvent = eventChannel.receive()
            println("Player: ${player.id}: Action received")

            handleEvent(player, playerEvent)
        } while (playerEvent !is EndTurn)

        println("Player: ${player.id}: Turn is over")
    }

    private fun handleEvent(player: GamePlayer, playerIntent: StrandedPlayerIntent) {
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

    fun processEvent(change: StrandedStateChange) {
        _gameState.processEvent(change, gameEventHandler)
    }

    private fun clearGameState() {
        _gameState.apply {
            gamePlayers.clear()
            scavengeStack.clear()
            nightStack.clear()
            belongingsStack.clear()
            shelters.clear()
            hasFire = false
            isFireBlocked = false
            night = 1
            targetList.clear()
            fireDamageMod = 0
            phase = Phase.NIGHT
        }
    }
}
