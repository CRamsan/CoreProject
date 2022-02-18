package com.cramsan.stranded.lib.game.logic

import com.cramsan.stranded.lib.game.intent.Consume
import com.cramsan.stranded.lib.game.intent.Craft
import com.cramsan.stranded.lib.game.intent.EndTurn
import com.cramsan.stranded.lib.game.intent.Forage
import com.cramsan.stranded.lib.game.intent.SelectCard
import com.cramsan.stranded.lib.game.intent.StrandedPlayerIntent
import com.cramsan.stranded.lib.game.intent.Transfer
import com.cramsan.stranded.lib.game.models.GamePlayer
import com.cramsan.stranded.lib.game.models.common.Belongings
import com.cramsan.stranded.lib.game.models.common.Phase
import com.cramsan.stranded.lib.game.models.common.Weapon
import com.cramsan.stranded.lib.game.models.night.CancellableByFire
import com.cramsan.stranded.lib.game.models.night.CancellableByWeapon
import com.cramsan.stranded.lib.game.models.night.DamageToDo
import com.cramsan.stranded.lib.game.models.night.DestroyShelter
import com.cramsan.stranded.lib.game.models.night.FiberLost
import com.cramsan.stranded.lib.game.models.night.FireUnavailableTomorrow
import com.cramsan.stranded.lib.game.models.night.ForageCardLost
import com.cramsan.stranded.lib.game.models.night.NightEvent
import com.cramsan.stranded.lib.game.models.night.SelectTargetOnlyUnsheltered
import com.cramsan.stranded.lib.game.models.night.SelectTargetQuantity
import com.cramsan.stranded.lib.game.models.night.SelectTargetQuantityAll
import com.cramsan.stranded.lib.game.models.night.Survived
import com.cramsan.stranded.lib.game.models.scavenge.Resource
import com.cramsan.stranded.lib.game.models.scavenge.ResourceType
import com.cramsan.stranded.lib.game.models.scavenge.ScavengeResult
import com.cramsan.stranded.lib.game.models.state.StrandedStateChange
import com.cramsan.stranded.lib.game.models.state.CraftCard
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
    private val startingNightCards: List<NightEvent>,
    private val startingForageCards: List<ScavengeResult>,
    private val startingBelongingCards: List<Belongings>,
) : MultiplayerGame {

    private lateinit var playerIntents: Map<String, Channel<StrandedPlayerIntent>>

    private var _gameEventHandler: GameEventHandler? = null

    private var _multiplayerGameEventHandler: MultiplayerGameEventHandler? = null

    private val _gameState = MutableStrandedGameState(
        mutableListOf(),
        mutableListOf(),
        mutableListOf(),
        mutableListOf(),
    )

    private var _gameStateSnapshot = StrandedGameState.EMPTY_STATE
    override val gameState: StrandedGameState = _gameStateSnapshot

    var gameCompleted = false
        private set

    override fun onConfigureGame(playerList: List<Player>) {
       configureGame(playerList)
    }

    override fun onGameStarted() {
        startGameJob()
    }

    override fun onGameEnded() {
    }

    override fun onPlayerIntentReceived(playerId: String, playerIntent: PlayerIntent) {
        gameScope.launch {
            playerIntents[playerId]?.send(playerIntent as StrandedPlayerIntent)
        }
    }

    override fun registerServerEventHandler(eventHandler: MultiplayerGameEventHandler) {
        _multiplayerGameEventHandler = eventHandler
    }

    override fun deregisterServerEventHandler() {
        _multiplayerGameEventHandler = null
    }


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
            phase = newGameState.phase
        }
        _gameStateSnapshot = MutableStrandedGameState.toSnapshot(_gameState)
    }

    private fun configureGame(players: List<Player>) {
        clearGameState()
        _gameState.gamePlayers.addAll(players.map {
            GamePlayer(
                it.id,
                it.name,
                4,
            )
        })
        _gameState.scavengeStack.addAll(startingForageCards)
        _gameState.nightStack.addAll(startingNightCards)
        _gameState.belongingsStack.addAll(startingBelongingCards)
        _gameStateSnapshot = MutableStrandedGameState.toSnapshot(_gameState)
        playerIntents = gameState.gamePlayers.associate {
            it.id to Channel(
                capacity = Channel.UNLIMITED,
            )
        }
    }

    private fun startGameJob() {
        gameScope.launch(exceptionHandler) {
            dealInitialHand()

            while (gameState.nightStack.isNotEmpty()) {
                performNightPhase()
                if (gameCompleted) {
                    break
                }
                performDayPhase()
            }

            println("Game is over")
        }
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

    internal suspend fun processNightEvent(nightEvent: NightEvent) {
        processEvent(SetFireBlockStatus(false))
        val targetList = mutableListOf<GamePlayer>()

        val statements = nightEvent.statements.sortedBy { it.priority }

        for (statement in statements) {
            when (statement) {
                CancellableByFire -> if (gameState.hasFire) {
                    break
                }
                DestroyShelter -> {
                    processEvent(com.cramsan.stranded.lib.game.models.state.DestroyShelter)
                }
                FireUnavailableTomorrow -> {
                    processEvent(SetFireBlockStatus(true))
                }
                is SelectTargetOnlyUnsheltered -> {
                    val sheltered = gameState.shelters.map { it.playerList }.flatten()
                    targetList.clear()
                    targetList.addAll(gameState.gamePlayers.filter { !sheltered.contains(it.id) })
                }
                is SelectTargetQuantity -> {
                    targetList.clear()
                    targetList.addAll(gameState.gamePlayers.shuffled().subList(0, statement.affectedPlayers))
                }
                SelectTargetQuantityAll -> {
                    targetList.clear()
                    targetList.addAll(gameState.gamePlayers)
                }
                is CancellableByWeapon -> targetList.waitForAll { gamePlayer, intent ->
                    when (intent) {
                        is SelectCard -> {
                            if (gamePlayer.id != intent.playerId) {
                                false
                            } else if (intent.cardId.isEmpty()) {
                                processEvent(SingleHealthChange(gamePlayer.id, statement.damage))
                                true
                            } else if (getCard(gamePlayer, intent.cardId) !is Weapon) {
                                false
                            } else {
                                processEvent(UserCard(gamePlayer.id, intent.cardId))
                                processEvent(SingleHealthChange(gamePlayer.id, statement.damage + statement.change))
                                true
                            }
                        }
                        else -> false
                    }
                }
                is ForageCardLost -> {
                    targetList.forEach { gamePlayer ->
                        val cardList = gamePlayer.scavengeResults.map { it.id }.shuffled()
                        cardList.subList(0, minOf(cardList.size, statement.cardsLost)).forEach { cardId ->
                            processEvent(LoseCard(gamePlayer.id, cardId))
                        }
                    }
                }
                FiberLost -> {
                    targetList.forEach { gamePlayer ->
                        gamePlayer.scavengeResults.filter { it is Resource && it.resourceType == ResourceType.FIBER }.forEach { card ->
                            processEvent(LoseCard(gamePlayer.id, card.id))
                        }
                    }
                }
                is DamageToDo -> {
                    targetList.forEach { gamePlayer ->
                        processEvent(SingleHealthChange(gamePlayer.id, statement.healthChange))
                    }
                }
                Survived -> gameCompleted = true
            }
        }

        processEvent(ExtinguishFire)

        gameState.gamePlayers.waitForAll { _, intent ->
            intent is EndTurn
        }
    }

    // MOve out of class
    private suspend fun List<GamePlayer>.waitForAll(predicate: suspend (GamePlayer, StrandedPlayerIntent) -> Boolean) {
        this.map { gamePlayer ->
            gameScope.launch(exceptionHandler) {
                val eventChannel = playerIntents.getValue(gamePlayer.id)
                while (true) {
                    val playerEvent = eventChannel.receive()
                    if (predicate(gamePlayer, playerEvent))
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
            is SelectCard -> TODO()
        }
    }

    fun processEvent(change: StrandedStateChange) {
        val newSnapshot = _gameState.processEvent(change, _multiplayerGameEventHandler, _gameEventHandler)
        _gameStateSnapshot = newSnapshot
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
            phase = Phase.NIGHT
        }
        _gameStateSnapshot = MutableStrandedGameState.toSnapshot(_gameState)
    }
}
