package com.cramsan.stranded.cli.gamemenu

import com.cramsan.stranded.cli.CliScreen
import com.cramsan.stranded.cli.NavigationCommand
import com.cramsan.stranded.lib.client.Client
import com.cramsan.stranded.lib.client.controllers.DefaultGameController
import com.cramsan.stranded.lib.client.controllers.DefaultMainMenuController
import com.cramsan.stranded.lib.client.controllers.GameController
import com.cramsan.stranded.lib.client.controllers.GameControllerEventHandler
import com.cramsan.stranded.lib.client.controllers.MainMenuEventHandler
import com.cramsan.stranded.lib.client.ui.game.GameScreenEventHandler
import com.cramsan.stranded.lib.client.ui.game.widget.CraftingUIWidget
import com.cramsan.stranded.lib.client.ui.game.widget.NightCardWidget
import com.cramsan.stranded.lib.client.ui.game.widget.PhaseComponentWidget
import com.cramsan.stranded.lib.client.ui.game.widget.PlayerHandWidget
import com.cramsan.stranded.lib.client.ui.game.widget.PlayerHeartsWidget
import com.cramsan.stranded.lib.client.ui.game.widget.PlayerListWidget
import com.cramsan.stranded.lib.client.ui.game.widget.ShelterWidget
import com.cramsan.stranded.lib.client.ui.mainmenu.LobbyListMenu
import com.cramsan.stranded.lib.client.ui.mainmenu.LobbyMenu
import com.cramsan.stranded.lib.client.ui.mainmenu.PlayerNameMenu
import com.cramsan.stranded.lib.client.ui.widget.BackgroundWidget
import com.cramsan.stranded.lib.game.models.GamePlayer
import com.cramsan.stranded.lib.game.models.common.Card
import com.cramsan.stranded.lib.game.models.common.Phase
import com.cramsan.stranded.lib.game.models.crafting.Shelter
import com.cramsan.stranded.lib.repository.GameScope
import com.cramsan.stranded.lib.repository.Lobby
import com.cramsan.stranded.lib.repository.Player
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class GameScreen(
    private val playerId: String,
    private val playerList: List<Player>,
    private val lobbyId: String,
    client: Client,
    scope: CoroutineScope,
) : CliScreen, PlayerListWidget, PlayerHeartsWidget, PlayerHandWidget, ShelterWidget, PhaseComponentWidget,
    NightCardWidget, CraftingUIWidget, GameControllerEventHandler, BackgroundWidget {

    private var controller: GameController

    private var _hearts = 0

    private val _hand = mutableListOf<Card>()

    init {
        controller = DefaultGameController(
            client,
            scope,
            GameScope(Dispatchers.Default),
        )
    }

    override fun startScreen() {
        controller.onShow()
        controller.configureController(
            playerId,
            playerList,
            lobbyId,
            this,
            this,
            this,
            this,
            this,
            this,
            this,
            this,
            this,
            this,
        )

        /*
        controller.let {
            crafting.eventHandler = it
            hand.eventHandler = it
            pauseMenu.eventHandler = it
        }
         */
    }

    override fun processInput(): NavigationCommand {
        println(availableCommandsMessage())
        var command: GameCommand?
        do {
            command = readlnOrNull()?.let { commandFromString(it) }
            val arg = readArgumentIfNeeded(command)
            handleCommand(command, arg)
        }
        while (command != GameCommand.EXIT)

        return NavigationCommand.GoToMainMenu
    }

    private fun handleCommand(command: GameCommand?, arg: String?) {
        when (command) {
            GameCommand.SPEND_ENERGY -> {
                val energyToSpend = arg?.toInt() ?: TODO()
                if (energyToSpend >= _hearts) {
                    println("Cannot spend more than $_hearts")
                    return
                }
                _hearts -= energyToSpend
                controller.onReadyButtonPressed()
            }
            GameCommand.SET_READY -> controller.onReadyButtonPressed()
            GameCommand.SET_NOT_READY -> controller.onReadyButtonPressed()
            GameCommand.LIST_HAND -> {
                println("Hand contents:")
                _hand.forEach {
                    println("\t${it}")
                }
            }
            GameCommand.PLAY_CARD -> {
                val card = _hand.find {
                    it.id == arg
                }
                if (card == null) {
                    println("Card with Id $arg not found")
                    return
                }
                controller.onCardSelected(card)
            }
            GameCommand.HELP -> println(availableCommandsMessage())
            GameCommand.EXIT -> TODO()
            null -> Unit
        }
    }

    private fun readArgumentIfNeeded(command: GameCommand?): String? = when (command) {
        GameCommand.PLAY_CARD, GameCommand.SPEND_ENERGY -> readln()
        GameCommand.SET_READY, GameCommand.SET_NOT_READY,GameCommand.LIST_HAND, GameCommand.HELP, GameCommand.EXIT, null -> null
    }

    override fun stopScreen() {
        controller.onDispose()
    }

    override fun setPlayerList(playerList: List<GamePlayer>) {
        println("Players:")
        playerList.forEach {
            println("\t$it")
        }
    }

    override fun updatePlayer(player: Player) {
        println("Player updated: $player")
    }

    override val hearts: Int
        get() = _hearts

    override fun setHeartsContent(player: GamePlayer) {
        _hearts = player.health
    }

    override fun setEnabled(enabled: Boolean) {
    }

    override fun setHandContent(player: GamePlayer) {
        _hand.clear()
        val cards = player.belongings + player.scavengeResults + player.craftables
        _hand.addAll(cards)
    }

    override fun addCard(card: Card) {
        _hand.add(card)
    }

    override fun removeCard(card: Card) {
        _hand.remove(card)
    }


    override fun setPhaseForHand(gamePhase: Phase) {
    }

    override fun setShelterList(shelterList: List<Shelter>) {
        TODO("Not yet implemented")
    }

    override fun displayCard(card: Card) {
        println("NIGHT CARD: $card")
    }

    override fun hideCard() {
    }

    override fun setPhaseForBackground(phase: Phase) {
    }

    override fun setPhaseForCrafting(gamePhase: Phase) {
    }

    override fun setPhase(phase: Phase) {
        println("Phase: $phase")
    }

    override fun setVisible(isVisible: Boolean) {
    }

    override fun onExitGameSelected() {
        TODO("Not yet implemented")
    }
}