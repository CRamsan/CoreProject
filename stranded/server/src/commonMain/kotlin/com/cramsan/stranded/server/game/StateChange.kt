package com.cramsan.stranded.server.game

import kotlinx.serialization.Serializable

/**
 * This class will be used as a serializable message that contains a message that will be applied to change the state
 * of the game.
 *
 * @author cramsan
 */
@Serializable
abstract class StateChange
