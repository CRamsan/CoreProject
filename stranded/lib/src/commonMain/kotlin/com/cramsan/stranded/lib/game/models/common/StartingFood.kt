package com.cramsan.stranded.lib.game.models.common

import kotlinx.serialization.Serializable

/**
 * TODO: We should change the name of this class to something more descriptive.
 *
 * This class represents a type of [Belongings] of type [Food].
 */
@Serializable
data class StartingFood(
    override val title: String,
    override var remainingDays: Int,
    override var healthModifier: Int,
    override var statusModifier: Status,
    override var remainingUses: Int,
) : Food, Belongings()
