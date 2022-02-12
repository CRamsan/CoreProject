package com.cramsan.stranded.lib.game.intent

import com.cramsan.stranded.lib.game.logic.StrandedGameState
import com.cramsan.stranded.lib.game.models.common.Card
import com.cramsan.stranded.lib.game.models.common.Phase
import com.cramsan.stranded.lib.game.models.crafting.Craftable
import com.cramsan.stranded.lib.game.models.scavenge.ScavengeResult
import com.cramsan.stranded.lib.game.models.state.StrandedStateChange
import com.cramsan.stranded.server.game.PlayerIntent
import kotlinx.serialization.Serializable

/**
 * The [StrandedPlayerIntent] represents a request from a user to perform an action. This class is intended to be sent from the
 * client, usually as a result from a UI event. Since a client is allowed to send [StrandedPlayerIntent] at any time, the server
 * will determine which [StrandedPlayerIntent] is allowed to change the [StrandedGameState]. Any change performed as a result of a [StrandedPlayerIntent]
 * will be received by the clients as a [StrandedStateChange].
 */
@Serializable
sealed class StrandedPlayerIntent : PlayerIntent()

/**
 * User is requesting to exchange [amount] health for the same number of [ScavengeResult].
 */
@Serializable
data class Forage(val amount: Int) : StrandedPlayerIntent()

/**
 * The [cardId] of the card that a user is intending to use. The [cardId] should match the [Card.id] of a card that is owned by the player
 * sending the [StrandedPlayerIntent].
 */
@Serializable
data class Consume(val cardId: String) : StrandedPlayerIntent()

/**
 * The [cardId] of the card that a user is intending to transfer and the [targetPlayerId] for the receiving player.
 * The [cardId] should match the [Card.id] of a card that is owned by the player sending the [StrandedPlayerIntent].
 */
@Serializable
data class Transfer(val cardId: String, val targetPlayerId: String) : StrandedPlayerIntent()

/**
 * The [targetList] should contain the [Card.id] of the cards that a user is intending to exchange for an instance
 * of [craftable]. The Ids in [targetList] should match a cards that are owned by the player sending the [StrandedPlayerIntent].
 */
@Serializable
data class Craft(val targetList: List<String>, val craftable: Craftable) : StrandedPlayerIntent()

/**
 * The user intents to complete the current [Phase].
 */
@Serializable
object EndTurn : StrandedPlayerIntent()
