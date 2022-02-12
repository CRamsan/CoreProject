package com.cramsan.stranded.server.game

import kotlinx.serialization.Serializable

/**
 * The [PlayerIntent] represents a request from a user to perform an action. This class is intended to be sent from the
 * client, usually as a result from a UI event. Since a client is allowed to send [PlayerIntent] at any time, the server
 * will determine which [PlayerIntent] is allowed to change the [GameState]. Any change performed as a result of a
 * [PlayerIntent] will be received by the clients as a [StateChange].
 *
 * Each game implementation will define their own classes that extend from [PlayerIntent].
 */
@Serializable
abstract class PlayerIntent
