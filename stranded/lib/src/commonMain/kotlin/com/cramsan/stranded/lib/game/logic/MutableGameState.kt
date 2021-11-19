package com.cramsan.stranded.lib.game.logic

import com.cramsan.stranded.lib.game.models.GamePlayer
import com.cramsan.stranded.lib.game.models.common.Belongings
import com.cramsan.stranded.lib.game.models.common.Phase
import com.cramsan.stranded.lib.game.models.crafting.Shelter
import com.cramsan.stranded.lib.game.models.night.NightEvent
import com.cramsan.stranded.lib.game.models.scavenge.ScavengeResult
import kotlinx.serialization.Serializable

/**
 * This class represents the entire state of the game.
 * This class does not hold any game logic. To make change you should call [processEvent].
 */
@Serializable
data class MutableGameState(
    override var gamePlayers: List<GamePlayer>,
    override var scavengeStack: MutableList<ScavengeResult>,
    override var nightStack: MutableList<NightEvent>,
    override var belongingsStack: MutableList<Belongings>,
    override var shelters: MutableList<Shelter> = mutableListOf(),
    override var hasFire: Boolean = false,
    override var isFireBlocked: Boolean = false,
    override var night: Int = 1,
    override var targetList: List<GamePlayer>? = null,
    override var fireDamageMod: Int = 0,
    override var phase: Phase = Phase.NIGHT
) : GameState
