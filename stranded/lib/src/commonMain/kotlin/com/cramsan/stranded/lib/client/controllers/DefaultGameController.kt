package com.cramsan.stranded.lib.client.controllers

import com.cramsan.stranded.lib.client.Client
import com.cramsan.stranded.lib.client.UIComponent
import com.cramsan.stranded.lib.client.ui.game.widget.NightCardWidget
import com.cramsan.stranded.lib.client.ui.game.widget.PhaseComponentWidget
import com.cramsan.stranded.lib.client.ui.game.widget.PlayerHandWidget
import com.cramsan.stranded.lib.client.ui.game.widget.PlayerHeartsWidget
import com.cramsan.stranded.lib.client.ui.game.widget.PlayerListWidget
import com.cramsan.stranded.lib.client.ui.game.widget.ShelterWidget
import com.cramsan.stranded.lib.game.intent.Consume
import com.cramsan.stranded.lib.game.intent.Craft
import com.cramsan.stranded.lib.game.intent.EndTurn
import com.cramsan.stranded.lib.game.intent.Forage
import com.cramsan.stranded.lib.game.logic.Game
import com.cramsan.stranded.lib.game.logic.GameState
import com.cramsan.stranded.lib.game.logic.getPlayer
import com.cramsan.stranded.lib.game.models.GamePlayer
import com.cramsan.stranded.lib.game.models.common.Card
import com.cramsan.stranded.lib.game.models.common.Phase
import com.cramsan.stranded.lib.game.models.crafting.Spear
import com.cramsan.stranded.lib.game.models.scavenge.Resource
import com.cramsan.stranded.lib.game.models.scavenge.ResourceType
import com.cramsan.stranded.lib.game.models.state.AllHealthChange
import com.cramsan.stranded.lib.game.models.state.CancellableByFire
import com.cramsan.stranded.lib.game.models.state.CancellableByFood
import com.cramsan.stranded.lib.game.models.state.CancellableByWeapon
import com.cramsan.stranded.lib.game.models.state.Change
import com.cramsan.stranded.lib.game.models.state.CraftCard
import com.cramsan.stranded.lib.game.models.state.DestroyShelter
import com.cramsan.stranded.lib.game.models.state.DrawBelongingCard
import com.cramsan.stranded.lib.game.models.state.DrawNightCard
import com.cramsan.stranded.lib.game.models.state.DrawScavengeCard
import com.cramsan.stranded.lib.game.models.state.FiberLost
import com.cramsan.stranded.lib.game.models.state.FireModification
import com.cramsan.stranded.lib.game.models.state.FireUnavailableTomorrow
import com.cramsan.stranded.lib.game.models.state.ForageCardLost
import com.cramsan.stranded.lib.game.models.state.IncrementNight
import com.cramsan.stranded.lib.game.models.state.MultiHealthChange
import com.cramsan.stranded.lib.game.models.state.SelectTargetOnlyUnsheltered
import com.cramsan.stranded.lib.game.models.state.SelectTargetQuantity
import com.cramsan.stranded.lib.game.models.state.SelectTargetQuantityAll
import com.cramsan.stranded.lib.game.models.state.SetPhase
import com.cramsan.stranded.lib.game.models.state.SingleHealthChange
import com.cramsan.stranded.lib.game.models.state.Survived
import com.cramsan.stranded.lib.game.models.state.UserCard
import com.cramsan.stranded.lib.messages.GameChange
import com.cramsan.stranded.lib.messages.GamePlayerIntent
import com.cramsan.stranded.lib.messages.GameStateMessage
import com.cramsan.stranded.lib.messages.ReadyToStartGame
import com.cramsan.stranded.lib.messages.ServerEvent
import com.cramsan.stranded.lib.repository.GameScope
import com.cramsan.stranded.lib.repository.Player
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DefaultGameController(
    private val client: Client,
    private val mainScope: CoroutineScope,
    private val gameScope: GameScope,
) : GameController {

    var mode: GameMode = GameMode.Game
        set(value) {
            setMenuMode(value)
        }

    lateinit var playerListUI: PlayerListWidget
    lateinit var playerHeartsWidget: PlayerHeartsWidget
    lateinit var handUI: PlayerHandWidget
    lateinit var shelterUI: ShelterWidget
    lateinit var phaseUI: PhaseComponentWidget
    lateinit var pauseMenu: UIComponent
    lateinit var nightCardUI: NightCardWidget
    lateinit var gameControllerEventHandler: GameControllerEventHandler

    lateinit var playerId: String
    lateinit var playerList: List<Player>
    lateinit var lobbyId: String
    override lateinit var game: Game

    var nightCard: Card? = null

    var startingHealth = 0

    override fun configureController(
        playerId: String,
        playerList: List<Player>,
        lobbyId: String,
        playerListUI: PlayerListWidget,
        playerHeartsWidget: PlayerHeartsWidget,
        handUI: PlayerHandWidget,
        shelterUI: ShelterWidget,
        phaseUI: PhaseComponentWidget,
        nightCardUI: NightCardWidget,
        pauseMenu: UIComponent,
        gameControllerEventHandler: GameControllerEventHandler,
    ) {
        this.playerListUI = playerListUI
        this.playerHeartsWidget = playerHeartsWidget
        this.handUI = handUI
        this.shelterUI = shelterUI
        this.phaseUI = phaseUI
        this.nightCardUI = nightCardUI
        this.pauseMenu = pauseMenu
        this.gameControllerEventHandler = gameControllerEventHandler

        this.playerId = playerId
        this.playerList = playerList
        this.lobbyId = lobbyId

        game = Game(gameScope.scope)
        game.gameEventHandler = this
        game.configureGame(
            playerList,
            listOf(),
            listOf(),
            listOf(),
        )
    }

    override fun onShow() {
        client.registerListener(this)
        mode = GameMode.Game
        client.sendMessage(ReadyToStartGame)
    }

    override fun onDispose() {
        gameScope.endScope()
        client.deregisterListener(this)
    }

    override fun onServerEventReceived(serverEvent: ServerEvent) {
        mainScope.launch {
            when (serverEvent) {
                is GameChange -> {
                    game.processEvent(serverEvent.change)
                }
                is GameStateMessage -> handleGameStateMessage(serverEvent.gameState)
                else -> Unit
            }
        }
    }

    override fun handleGameStateMessage(gameState: GameState) {
        // TODO: Fix this, we are creating a copy every time.
        game.setGameState(gameState)
        playerListUI.setPlayerList(gameState.gamePlayers)
        playerHeartsWidget.setContent(gameState.gamePlayers.first())
        handUI.setContent(gameState.gamePlayers.first())
        // shelterUI.setContent(gameState.shelters)
    }

    override fun setMenuMode(mode: GameMode) {
        pauseMenu.setVisible(mode == GameMode.Pause)
    }

    override fun exitGame() {
        gameControllerEventHandler.onExitGameSelected()
    }

    override fun createSpear() {
        val player = game.gameState.gamePlayers.findPlayer()

        val rock = player.scavengeResults.find {
            if (it is Resource) {
                it.resourceType == ResourceType.ROCK
            } else {
                false
            }
        }
        val stick = player.scavengeResults.find {
            if (it is Resource) {
                it.resourceType == ResourceType.STICK
            } else {
                false
            }
        }

        if (rock != null && stick != null) {
            client.sendMessage(GamePlayerIntent(Craft(listOf(rock.id, stick.id), Spear())))
        }
    }

    override fun createBasket() {
        TODO("Not yet implemented")
    }

    override fun createShelter() {
        TODO("Not yet implemented")
    }

    override fun onEventHandled(change: Change) {
        when (change) {
            is AllHealthChange -> Unit
            CancellableByFire -> Unit
            is CancellableByFood -> Unit
            is CancellableByWeapon -> Unit
            DestroyShelter -> Unit
            DrawNightCard -> {
            }
            is DrawScavengeCard, is DrawBelongingCard, is UserCard -> Unit
            FiberLost -> Unit
            is FireModification -> Unit
            FireUnavailableTomorrow -> Unit
            is ForageCardLost -> Unit
            IncrementNight -> Unit
            is SelectTargetOnlyUnsheltered -> Unit
            is SelectTargetQuantity -> Unit
            SelectTargetQuantityAll -> Unit
            is SetPhase -> {
                phaseUI.setPhase(change.gamePhase)
                if (change.gamePhase == Phase.FORAGING) {
                    nightCardUI.hideCard()
                    startingHealth = game.gameState.gamePlayers.findPlayer().health
                    playerHeartsWidget.setEnabled(true)
                } else {
                    if (change.gamePhase == Phase.NIGHT) {
                        nightCard = game.gameState.nightStack.last()
                        nightCardUI.displayCard(nightCard!!)
                    }
                    playerHeartsWidget.setEnabled(false)
                }
            }
            is MultiHealthChange -> Unit
            is SingleHealthChange -> Unit
            Survived -> Unit
            is CraftCard -> Unit
        }
    }

    fun List<GamePlayer>.findPlayer(): GamePlayer {
        return find { it.id == playerId }!!
    }

    override fun onCardSelected(card: Card) {
        client.sendMessage(GamePlayerIntent(Consume(card.id)))
    }

    override fun onPlayerHealthChange(playerId: String, health: Int) {
        playerListUI.setPlayerList(game.gameState.gamePlayers)
        if (playerId == client.player.id) {
            playerHeartsWidget.setContent(game.gameState.getPlayer(playerId))
        }
    }

    override fun onCardReceived(playerId: String, card: Card) {
        if (playerId == client.player.id) {
            handUI.addCard(card)
        }
    }

    override fun onCardRemoved(playerId: String, card: Card) {
        if (playerId == client.player.id) {
            handUI.removeCard(card)
        }
    }

    override fun onResumeGameSelected() {
        TODO("Not yet implemented")
    }

    override fun onExitGameSelected() {
        gameControllerEventHandler.onExitGameSelected()
    }

    override fun onReadyButtonPressed() {
        when (game.gameState.phase) {
            Phase.FORAGING -> {
                val foragingCost = startingHealth - playerHeartsWidget.hearts
                client.sendMessage(GamePlayerIntent(Forage(foragingCost)))
            }
            Phase.NIGHT_PREPARE -> client.sendMessage(GamePlayerIntent(EndTurn))
            Phase.NIGHT -> client.sendMessage(GamePlayerIntent(EndTurn))
        }
    }
}
