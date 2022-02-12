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
@Serializable
class MutableStrandedGameState(
    override val gamePlayers: MutableList<GamePlayer>,
    override val scavengeStack: MutableList<ScavengeResult>,
    override val nightStack: MutableList<NightEvent>,
    override val belongingsStack: MutableList<Belongings>,
    override val shelters: MutableList<Shelter> = mutableListOf(),
    override var hasFire: Boolean = false,
    override var isFireBlocked: Boolean = false,
    override var night: Int = 1,
    override val targetList: MutableList<GamePlayer> = mutableListOf(),
    override var fireDamageMod: Int = 0,
    override var phase: Phase = Phase.NIGHT
) : StrandedGameState
