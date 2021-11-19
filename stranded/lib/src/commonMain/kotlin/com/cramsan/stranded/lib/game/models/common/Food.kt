package com.cramsan.stranded.lib.game.models.common

/**
 * Interface for a class that can modify health upon consumption. Food will naturally decay, so it can only be consumed
 * while [remainingDays] is greater than 0. Upon each time this card is used, [healthModifier] will be applied to the
 * player's health and the [statusModifier] will be set to the player.
 */
interface Food : UsableItem {

    var remainingDays: Int

    val healthModifier: Int

    val statusModifier: Status
}
