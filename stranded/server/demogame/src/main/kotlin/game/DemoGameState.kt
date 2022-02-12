package com.cramsan.stranded.server.demoapp.game

import com.cramsan.stranded.server.game.GameState
import kotlinx.serialization.Serializable

/**
 * Class representing the state of the game across client and server. This "game" has a single variable [counter]
 * that counts when a user takes an action.
 *
 * @author cramsan
 */
@Serializable
data class DemoGameState(
    val counter: Int
) : GameState
