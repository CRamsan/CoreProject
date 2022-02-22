package com.cramsan.stranded.lib.game.logic

import com.cramsan.stranded.lib.game.models.GamePlayer
import com.cramsan.stranded.lib.game.models.common.Belongings
import com.cramsan.stranded.lib.game.models.common.Phase
import com.cramsan.stranded.lib.game.models.crafting.Shelter
import com.cramsan.stranded.lib.game.models.night.NightEvent
import com.cramsan.stranded.lib.game.models.scavenge.ScavengeResult
import com.cramsan.stranded.server.game.GameState
import kotlinx.serialization.Serializable

@Serializable
data class StrandedGameState(
    val gamePlayers: List<GamePlayer>,
    val scavengeStack: List<ScavengeResult>,
    val nightStack: List<NightEvent>,
    val belongingsStack: List<Belongings>,
    val shelters: List<Shelter>,
    val hasFire: Boolean,
    val isFireBlocked: Boolean,
    val night: Int,
    val phase: Phase,
) : GameState {
    companion object {
        val EMPTY_STATE = StrandedGameState(
            gamePlayers = emptyList(),
            scavengeStack = emptyList(),
            nightStack = emptyList(),
            belongingsStack = emptyList(),
            shelters = emptyList(),
            hasFire = false,
            isFireBlocked = false,
            night = 0,
            phase = Phase.NIGHT,
        )
    }
}
