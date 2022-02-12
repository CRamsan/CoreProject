package com.cramsan.stranded.lib.game.logic

import com.cramsan.stranded.lib.game.models.GamePlayer
import com.cramsan.stranded.lib.game.models.common.Belongings
import com.cramsan.stranded.lib.game.models.common.Phase
import com.cramsan.stranded.lib.game.models.crafting.Shelter
import com.cramsan.stranded.lib.game.models.night.NightEvent
import com.cramsan.stranded.lib.game.models.scavenge.ScavengeResult
import com.cramsan.stranded.server.game.GameState
import kotlinx.serialization.Serializable

/**
 * This class represents the entire state of the game.
 * This class does not hold any game logic. To make change you should call [processEvent].
 */
interface StrandedGameState : GameState {
    val gamePlayers: List<GamePlayer>
    val scavengeStack: List<ScavengeResult>
    val nightStack: List<NightEvent>
    val belongingsStack: List<Belongings>
    val shelters: List<Shelter>
    val hasFire: Boolean
    val isFireBlocked: Boolean
    val night: Int
    val targetList: List<GamePlayer>
    val fireDamageMod: Int
    val phase: Phase
}