package com.cramsan.stranded.lib.client.controllers

import com.cramsan.stranded.lib.client.UIComponent
import com.cramsan.stranded.lib.client.ui.game.widget.CraftingUIWidget
import com.cramsan.stranded.lib.client.ui.game.widget.NightCardWidget
import com.cramsan.stranded.lib.client.ui.game.widget.PhaseComponentWidget
import com.cramsan.stranded.lib.client.ui.game.widget.PlayerHandWidget
import com.cramsan.stranded.lib.client.ui.game.widget.PlayerHeartsWidget
import com.cramsan.stranded.lib.client.ui.game.widget.PlayerListWidget
import com.cramsan.stranded.lib.client.ui.game.widget.ShelterWidget
import com.cramsan.stranded.lib.client.ui.widget.BackgroundWidget
import com.cramsan.stranded.lib.game.intent.Consume
import com.cramsan.stranded.lib.game.intent.Craft
import com.cramsan.stranded.lib.game.intent.EndTurn
import com.cramsan.stranded.lib.game.intent.Forage
import com.cramsan.stranded.lib.game.logic.Game
import com.cramsan.stranded.lib.game.logic.StrandedGameState
import com.cramsan.stranded.lib.game.logic.getPlayer
import com.cramsan.stranded.lib.game.models.GamePlayer
import com.cramsan.stranded.lib.game.models.common.Card
import com.cramsan.stranded.lib.game.models.common.Phase
import com.cramsan.stranded.lib.game.models.crafting.Spear
import com.cramsan.stranded.lib.game.models.scavenge.Resource
import com.cramsan.stranded.lib.game.models.scavenge.ResourceType
import com.cramsan.stranded.lib.game.models.state.StrandedStateChange
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
import com.cramsan.stranded.lib.repository.GameScope
import com.cramsan.stranded.server.Client
import com.cramsan.stranded.server.MultiplayerGameEventHandler
import com.cramsan.stranded.server.game.StateChange
import com.cramsan.stranded.server.messages.GameChange
import com.cramsan.stranded.server.messages.GamePlayerIntent
import com.cramsan.stranded.server.messages.GameStateMessage
import com.cramsan.stranded.server.messages.ServerEvent
import com.cramsan.stranded.server.repository.Player
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DefaultGameController(
    private val client: Client,
    private val mainScope: CoroutineScope,
    private val gameScope: GameScope,
) : GameController, MultiplayerGameEventHandler {

    private var mode: GameMode = GameMode.Game

    lateinit var playerListUI: PlayerListWidget
    lateinit var playerHeartsWidget: PlayerHeartsWidget
    lateinit var handUI: PlayerHandWidget
    lateinit var shelterUI: ShelterWidget
    lateinit var phaseUI: PhaseComponentWidget
    lateinit var pauseMenu: UIComponent
    lateinit var nightCardUI: NightCardWidget
    lateinit var gameControllerEventHandler: GameControllerEventHandler
    lateinit var backgroundWidget: BackgroundWidget
    lateinit var craftingUI: CraftingUIWidget

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
        craftingUI: CraftingUIWidget,
        pauseMenu: UIComponent,
        gameControllerEventHandler: GameControllerEventHandler,
        backgroundWidget: BackgroundWidget
    ) {
        this.playerListUI = playerListUI
        this.playerHeartsWidget = playerHeartsWidget
        this.handUI = handUI
        this.shelterUI = shelterUI
        this.phaseUI = phaseUI
        this.nightCardUI = nightCardUI
        this.pauseMenu = pauseMenu
        this.gameControllerEventHandler = gameControllerEventHandler
        this.backgroundWidget = backgroundWidget
        this.craftingUI = craftingUI

        this.playerId = playerId
        this.playerList = playerList
        this.lobbyId = lobbyId

        game = Game(
            gameScope.scope,
            emptyList(),
            emptyList(),
            emptyList()
        )
    }

    override fun onShow() {
        client.registerListener(this)
        mode = GameMode.Game
    }

    override fun onDispose() {
        gameScope.endScope()
        client.deregisterListener(this)
    }

    override fun onServerEventReceived(serverEvent: ServerEvent) {
        mainScope.launch {
            when (serverEvent) {
                is GameChange -> {
                    val change = serverEvent.change as StrandedStateChange
                    game.processEvent(change)
                }
                is GameStateMessage -> {
                    val gameState = serverEvent.gameState as StrandedGameState
                    handleGameStateMessage(gameState)
                }
                else -> Unit
            }
        }
    }

    override fun handleGameStateMessage(gameState: StrandedGameState) {
        // TODO: Fix this, we are creating a copy every time.
        game.setGameState(gameState)
        playerListUI.setPlayerList(gameState.gamePlayers)
        playerHeartsWidget.setHeartsContent(gameState.gamePlayers.first())
        handUI.setHandContent(gameState.gamePlayers.first())
        // shelterUI.setContent(gameState.shelters)
    }

    override fun setMenuMode(mode: GameMode) {
        pauseMenu.setVisible(mode == GameMode.Pause)
        this.mode = mode
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

    override fun onStateChangeExecuted(change: StateChange) {
        if (change !is StrandedStateChange) {
            return
        }
        when (change) {
            DrawNightCard -> {
            }
            is DrawScavengeCard, is DrawBelongingCard, is UserCard -> Unit
            IncrementNight -> Unit
            is SetPhase -> {
                phaseUI.setPhase(change.gamePhase)
                craftingUI.setPhaseForCrafting(change.gamePhase)
                backgroundWidget.setPhaseForBackground(change.gamePhase)
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
            is SingleHealthChange -> Unit
            is CraftCard -> Unit
            DestroyShelter -> Unit
            ExtinguishFire -> Unit
            is LoseCard -> Unit
            is SetFireBlockStatus -> Unit
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
        if (playerId == this.playerId) {
            game.gameState.getPlayer(playerId)?.let { playerHeartsWidget.setHeartsContent(it) }
        }
    }

    override fun onCardReceived(playerId: String, card: Card) {
        if (playerId == this.playerId) {
            handUI.addCard(card)
        }
    }

    override fun onCardRemoved(playerId: String, card: Card) {
        if (playerId == this.playerId) {
            handUI.removeCard(card)
        }
    }

    override fun onResumeGameSelected() {
        setMenuMode(GameMode.Game)
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
