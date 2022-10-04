package com.cramsan.stranded.server.demoapp.game

import com.cramsan.stranded.server.game.PlayerIntent
import kotlinx.serialization.Serializable

/**
 * This class extends [PlayerIntent] and provides the base for all player intents that are specific to this game.
 * This class is sealed to help developers handle all possible subclasses during branching paths.
 *
 * @author cramsan
 */
sealed class DemoPlayerIntent : PlayerIntent()

/**
 * Player is requesting the server to increment the counter by 1.
 */
@Serializable
object RequestIncrementCounter : DemoPlayerIntent()
