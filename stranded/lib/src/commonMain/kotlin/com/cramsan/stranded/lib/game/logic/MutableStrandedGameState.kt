package com.cramsan.stranded.lib.game.logic

import com.cramsan.stranded.lib.game.models.GamePlayer
import com.cramsan.stranded.lib.game.models.common.Belongings
import com.cramsan.stranded.lib.game.models.common.Phase
import com.cramsan.stranded.lib.game.models.crafting.Shelter
import com.cramsan.stranded.lib.game.models.night.NightEvent
import com.cramsan.stranded.lib.game.models.scavenge.ScavengeResult

/**
 * This class represents the entire state of the game.
 * This class does not hold any game logic. To make change you should call [processEvent].
 */
data class MutableStrandedGameState(
    val gamePlayers: MutableList<GamePlayer>,
    val scavengeStack: MutableList<ScavengeResult>,
    val nightStack: MutableList<NightEvent>,
    val belongingsStack: MutableList<Belongings>,
    val shelters: MutableList<Shelter> = mutableListOf(),
    var hasFire: Boolean = false,
    var isFireBlocked: Boolean = false,
    var night: Int = 1,
    var phase: Phase = Phase.NIGHT
) {
    companion object {
        fun toSnapshot(state: MutableStrandedGameState): StrandedGameState {
            return StrandedGameState(
                gamePlayers = state.gamePlayers,
                scavengeStack = state.scavengeStack,
                nightStack = state.nightStack,
                belongingsStack = state.belongingsStack,
                shelters = state.shelters,
                hasFire = state.hasFire,
                isFireBlocked = state.isFireBlocked,
                night = state.night,
                phase = state.phase,
            )
        }
    }
}
