package com.cramsan.stranded.lib.game.models.scavenge

import com.cramsan.stranded.lib.game.models.common.Card
import kotlinx.serialization.Serializable

/**
 * A player will get a [ScavengeResult] for every health point they spend during the [com.cramsan.stranded.lib.game.logic.Phase.FORAGING]
 * phase.
 */
@Serializable
sealed class ScavengeResult() : Card()
