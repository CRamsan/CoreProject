package com.cramsan.stranded.server

import com.cramsan.stranded.server.game.MultiplayerGame

/**
 * @author cramsan
 */
interface MultiplayerGameFactory {

    fun buildMultiplayerGame(): MultiplayerGame
}
