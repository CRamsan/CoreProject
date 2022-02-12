package com.cramsan.stranded.lib.game.models

import com.cramsan.stranded.lib.game.models.common.Belongings
import com.cramsan.stranded.lib.game.models.crafting.Craftable
import com.cramsan.stranded.lib.game.models.scavenge.ScavengeResult
import kotlinx.serialization.Serializable

/**
 * This class represents a player within the game. The [id] should be the same as the
 * [com.cramsan.stranded.lib.repository.Player.id] of the player it represents. The [nane] is the string that will be
 * displayed to other users. The player will be able to play while they are able to end the
 * [com.cramsan.stranded.lib.game.logic.Phase.NIGHT] with [health] greater than 1.
 *
 * The [GamePlayer] has three sets of cards, the [belongings], [scavengeResults], [craftables]. These are all the cards
 * that this player owns.
 */
interface GamePlayer {
    val id: String
    val nane: String
    val health: Int
    val belongings: List<Belongings>
    val scavengeResults: List<ScavengeResult>
    val craftables: List<Craftable>
}
