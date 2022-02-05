package com.cramsan.stranded.cli

import com.cramsan.stranded.lib.repository.Player

sealed class NavigationCommand {

    object ExitGame : NavigationCommand()

    data class GoToGame(
        val playerId: String,
        val playerList: List<Player>,
        val lobbyId: String,
    ) : NavigationCommand()

    object GoToMainMenu : NavigationCommand()
}
