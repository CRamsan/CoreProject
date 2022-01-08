package com.cramsan.stranded.lib.client.controllers

import com.cramsan.stranded.lib.client.ClientEventHandler
import com.cramsan.stranded.lib.client.UIComponent
import com.cramsan.stranded.lib.client.ui.game.PauseMenuEventHandler
import com.cramsan.stranded.lib.client.ui.game.widget.CraftingUIWidget
import com.cramsan.stranded.lib.client.ui.game.widget.CraftingWidgetEventHandler
import com.cramsan.stranded.lib.client.ui.game.widget.NightCardWidget
import com.cramsan.stranded.lib.client.ui.game.widget.PhaseComponentWidget
import com.cramsan.stranded.lib.client.ui.game.widget.PlayerHandEventHandler
import com.cramsan.stranded.lib.client.ui.game.widget.PlayerHandWidget
import com.cramsan.stranded.lib.client.ui.game.widget.PlayerHeartsWidget
import com.cramsan.stranded.lib.client.ui.game.widget.PlayerListWidget
import com.cramsan.stranded.lib.client.ui.game.widget.ReadyWidgetEventHandler
import com.cramsan.stranded.lib.client.ui.game.widget.ShelterWidget
import com.cramsan.stranded.lib.client.ui.widget.BackgroundWidget
import com.cramsan.stranded.lib.game.logic.Game
import com.cramsan.stranded.lib.game.logic.GameEventHandler
import com.cramsan.stranded.lib.game.logic.GameState
import com.cramsan.stranded.lib.repository.Player

interface GameController :
    ClientEventHandler,
    CraftingWidgetEventHandler,
    PlayerHandEventHandler,
    GameEventHandler,
    PauseMenuEventHandler,
    ReadyWidgetEventHandler {

    val game: Game

    fun configureController(
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
        backgroundWidget: BackgroundWidget,
    )

    fun onShow()

    fun onDispose()

    fun handleGameStateMessage(gameState: GameState)

    fun setMenuMode(mode: GameMode)

    fun exitGame()
}
