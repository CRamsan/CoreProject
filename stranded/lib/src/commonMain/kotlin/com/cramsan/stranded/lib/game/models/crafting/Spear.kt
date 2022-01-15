package com.cramsan.stranded.lib.game.models.crafting

import com.cramsan.stranded.lib.game.models.common.Weapon
import kotlinx.serialization.Serializable

/**
 * This class represents a spear weapon that is crafted. It can be used to fend off attacks.
 */
@Serializable
class Spear : Craftable("Spear"), Weapon {
    override var remainingUses = 1
}
