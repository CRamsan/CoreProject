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
import com.cramsan.stranded.lib.game.logic.Game
import com.cramsan.stranded.lib.game.logic.MutableStrandedGameState
import com.cramsan.stranded.lib.game.logic.StrandedGameState
import com.cramsan.stranded.lib.game.models.GamePlayer
import com.cramsan.stranded.lib.game.models.common.Card
import com.cramsan.stranded.lib.game.models.common.Equippable
import com.cramsan.stranded.lib.game.models.common.Phase
import com.cramsan.stranded.lib.game.models.common.Status
import com.cramsan.stranded.lib.game.models.night.NightEvent
import com.cramsan.stranded.lib.game.models.scavenge.Consumable
import com.cramsan.stranded.lib.game.models.scavenge.Resource
import com.cramsan.stranded.lib.game.models.scavenge.ResourceType
import com.cramsan.stranded.lib.game.models.state.StrandedStateChange
import com.cramsan.stranded.lib.game.models.state.CraftCard
import com.cramsan.stranded.lib.game.models.state.DrawBelongingCard
import com.cramsan.stranded.lib.game.models.state.DrawNightCard
import com.cramsan.stranded.lib.game.models.state.DrawScavengeCard
import com.cramsan.stranded.lib.game.models.state.IncrementNight
import com.cramsan.stranded.lib.game.models.state.MultiHealthChange
import com.cramsan.stranded.lib.game.models.state.SetPhase
import com.cramsan.stranded.lib.game.models.state.SingleHealthChange
import com.cramsan.stranded.lib.game.models.state.UserCard
import com.cramsan.stranded.lib.repository.GameScope
import com.cramsan.stranded.server.messages.ServerEvent
import com.cramsan.stranded.server.repository.Player

open class DebugGameController(
    private val gameScope: GameScope,
) : GameController {

    private var mode: GameMode = GameMode.Game

    var phase = Phase.NIGHT

    var nightCard: Card? = null

    lateinit var playerListUI: PlayerListWidget
    lateinit var playerHeartsWidget: PlayerHeartsWidget
    lateinit var handUI: PlayerHandWidget
    lateinit var shelterUI: ShelterWidget
    lateinit var phaseUI: PhaseComponentWidget
    lateinit var nightCardUI: NightCardWidget
    lateinit var pauseMenu: UIComponent
    lateinit var gameControllerEventHandler: GameControllerEventHandler
    lateinit var backgroundWidget: BackgroundWidget
    lateinit var craftingUI: CraftingUIWidget

    override lateinit var game: Game

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

        game = Game(gameScope.scope)
        game.gameEventHandler = this

        val currentPlayer = GamePlayer(
            "",
            "cramsan",
            5,
        ).apply {
            belongings = mutableListOf(
                Equippable("Test item", 1),
            )
            scavengeResults = mutableListOf(
                Resource(ResourceType.FIBER),
                Consumable("Test", 1, 1, Status.NORMAL, 3),
            )
            craftables = mutableListOf()
        }

        val players = listOf(
            GamePlayer("", "Cramsan", 4),
            GamePlayer("", "Test", 2),
            GamePlayer("", "Dummy", 5),
            GamePlayer("", "Test2", 6),
            GamePlayer("", "A very long name!! :D@#$%^& jhgjhg KDDD", 1),
            GamePlayer("", "Howdy", 0),
            currentPlayer,
        )
        game.setGameState(
            MutableStrandedGameState(
                players.toMutableList(),
                mutableListOf(),
                mutableListOf(),
                mutableListOf(),
            )
        )

        phaseUI.setPhase(Phase.NIGHT)
        playerHeartsWidget.setHeartsContent(currentPlayer)
        playerHeartsWidget.setEnabled(true)
        playerListUI.setPlayerList(game.gameState.gamePlayers)
        handUI.setHandContent(currentPlayer)

        game.processEvent(SetPhase(Phase.NIGHT))
    }

    override fun onShow() = Unit

    override fun onDispose() = Unit

    override fun onServerEventReceived(serverEvent: ServerEvent) = Unit

    override fun handleGameStateMessage(gameState: StrandedGameState) = Unit

    override fun setMenuMode(mode: GameMode) {
        pauseMenu.setVisible(mode == GameMode.Pause)
        this.mode = mode
    }

    override fun exitGame() = Unit

    override fun createSpear() = Unit

    override fun createBasket() = Unit

    override fun createShelter() = Unit

    override fun onCardSelected(card: Card) = Unit

    override fun onEventHandled(change: StrandedStateChange) {
        when (change) {
            is SingleHealthChange -> Unit
            is MultiHealthChange -> Unit
            is DrawBelongingCard -> Unit
            is DrawScavengeCard -> Unit
            DrawNightCard -> Unit
            IncrementNight -> Unit
            is SetPhase -> {
                phase = change.gamePhase
                phaseUI.setPhase(change.gamePhase)
                craftingUI.setPhaseForCrafting(change.gamePhase)
                backgroundWidget.setPhaseForBackground(change.gamePhase)
                if (change.gamePhase == Phase.FORAGING) {
                    nightCardUI.hideCard()
                    playerHeartsWidget.setEnabled(true)
                } else {
                    if (change.gamePhase == Phase.NIGHT) {
                        nightCard = NightEvent(
                            "Bad event",
                            emptyList(),
                        )
                        nightCardUI.displayCard(nightCard!!)
                    }
                    playerHeartsWidget.setEnabled(false)
                }
                handUI.setPhaseForHand(change.gamePhase)
            }
            is UserCard -> Unit
            is CraftCard -> Unit
        }
    }

    override fun onPlayerHealthChange(playerId: String, health: Int) = Unit

    override fun onCardReceived(playerId: String, card: Card) = Unit

    override fun onCardRemoved(playerId: String, card: Card) = Unit

    override fun onResumeGameSelected() {
        setMenuMode(GameMode.Game)
    }

    override fun onExitGameSelected() {
        gameControllerEventHandler.onExitGameSelected()
    }

    override fun onReadyButtonPressed() {
        val newPhase = when (phase) {
            Phase.FORAGING -> Phase.NIGHT_PREPARE
            Phase.NIGHT_PREPARE -> Phase.NIGHT
            Phase.NIGHT -> Phase.FORAGING
        }
        onEventHandled(SetPhase(newPhase))
    }
}
