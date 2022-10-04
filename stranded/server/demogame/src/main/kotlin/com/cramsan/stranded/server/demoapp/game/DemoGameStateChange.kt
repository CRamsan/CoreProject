package com.cramsan.stranded.server.demoapp.game

import com.cramsan.stranded.server.game.StateChange
import kotlinx.serialization.Serializable

/**
 * This class extends [StateChange] to provide a game-specific list of state changes.
 *
 * @author cramsan
 */
sealed class DemoGameStateChange : StateChange()

/**
 * The [DemoGameState] will increment it's counter by 1.
 */
@Serializable
object IncrementCounter : DemoGameStateChange()
