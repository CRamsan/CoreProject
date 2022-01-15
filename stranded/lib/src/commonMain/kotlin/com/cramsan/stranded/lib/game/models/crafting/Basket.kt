package com.cramsan.stranded.lib.game.models.crafting

import kotlinx.serialization.Serializable

/**
 * A basket will allow the player who owns it to be able to get an extra
 * [com.cramsan.stranded.lib.game.models.common.ScavengeResult] during the [com.cramsan.stranded.lib.game.logic.Phase.FORAGING]
 * phase.
 */
@Serializable
class Basket : Craftable("Basket")
