package com.cramsan.stranded.lib.game.intent

import com.cramsan.stranded.lib.game.logic.GameState
import com.cramsan.stranded.lib.game.models.common.Card
import com.cramsan.stranded.lib.game.models.common.Phase
import com.cramsan.stranded.lib.game.models.crafting.Craftable
import com.cramsan.stranded.lib.game.models.scavenge.ScavengeResult
import com.cramsan.stranded.lib.game.models.state.Change
import kotlinx.serialization.Serializable

/**
 * The [PlayerIntent] represents a request from a user to perform an action. This class is intended to be sent from the
 * client, usually as a result from a UI event. Since a client is allowed to send [PlayerIntent] at any time, the server
 * will determine which [PlayerIntent] is allowed to change the [GameState]. Any change performed as a result of a [PlayerIntent]
 * will be received by the clients as a [Change].
 */
@Serializable
sealed class PlayerIntent

/**
 * User is requesting to exchange [amount] health for the same number of [ScavengeResult].
 */
@Serializable
data class Forage(val amount: Int) : PlayerIntent()

/**
 * The [cardId] of the card that a user is intending to use. The [cardId] should match the [Card.id] of a card that is owned by the player
 * sending the [PlayerIntent].
 */
@Serializable
data class Consume(val cardId: String) : PlayerIntent()

/**
 * The [cardId] of the card that a user is intending to transfer and the [targetPlayerId] for the receiving player.
 * The [cardId] should match the [Card.id] of a card that is owned by the player sending the [PlayerIntent].
 */
@Serializable
data class Transfer(val cardId: String, val targetPlayerId: String) : PlayerIntent()

/**
 * The [targetList] should contain the [Card.id] of the cards that a user is intending to exchange for an instance
 * of [craftable]. The Ids in [targetList] should match a cards that are owned by the player sending the [PlayerIntent].
 */
@Serializable
data class Craft(val targetList: List<String>, val craftable: Craftable) : PlayerIntent()

/**
 * The user intents to complete the current [Phase].
 */
@Serializable
object EndTurn : PlayerIntent()
