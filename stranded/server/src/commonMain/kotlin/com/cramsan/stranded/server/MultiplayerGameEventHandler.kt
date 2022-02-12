package com.cramsan.stranded.server

import com.cramsan.stranded.server.game.GameState
import com.cramsan.stranded.server.game.MultiplayerGame
import com.cramsan.stranded.server.game.StateChange

/**
 * Callback to listen for changes in the internal state of the [GameState].
 *
 * @author cramsan
 */
interface MultiplayerGameEventHandler {

    /**
     * Function called when the [MultiplayerGame] has executed a [change].
     */
    fun onStateChangeExecuted(change: StateChange)
}
