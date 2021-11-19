package com.cramsan.stranded.lib.game.models.common

/**
 * Interface for items that have a limited amount of use. This item can be used as long as [remainingUses] is greater
 * than 1.
 */
interface UsableItem {

    var remainingUses: Int
}
