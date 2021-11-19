package com.cramsan.stranded.lib.game.logic

import com.cramsan.stranded.lib.game.models.GamePlayer
import com.cramsan.stranded.lib.game.models.common.Belongings
import com.cramsan.stranded.lib.game.models.common.Phase
import com.cramsan.stranded.lib.game.models.crafting.Shelter
import com.cramsan.stranded.lib.game.models.night.NightEvent
import com.cramsan.stranded.lib.game.models.scavenge.ScavengeResult

sealed interface GameState {
    val gamePlayers: List<GamePlayer>
    val scavengeStack: List<ScavengeResult>
    val nightStack: List<NightEvent>
    val belongingsStack: List<Belongings>
    val shelters: List<Shelter>
    val hasFire: Boolean
    val isFireBlocked: Boolean
    val night: Int
    val targetList: List<GamePlayer>?
    val fireDamageMod: Int
    val phase: Phase
}
