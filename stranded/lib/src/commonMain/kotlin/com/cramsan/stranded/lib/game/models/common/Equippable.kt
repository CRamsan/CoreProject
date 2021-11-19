package com.cramsan.stranded.lib.game.models.common

import kotlinx.serialization.Serializable

/**
 * TODO: We should change the name of this class to something more descriptive.
 *
 * This class represents a type of [Belongings] that the player can use as a [Weapon].
 */
@Serializable
data class Equippable(
    override var remainingUses: Int,
) : Belongings(), Weapon
