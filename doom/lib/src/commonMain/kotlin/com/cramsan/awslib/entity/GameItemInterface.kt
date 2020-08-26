package com.cramsan.awslib.entity

import com.cramsan.awslib.map.GridPositionableInterface

/**
 * This interface defines a contract for elements that appear in the [com.cramsan.awslib.map.GameMap] and that
 * can be picked up.
 */
interface GameItemInterface : GridPositionableInterface {
    val id: Int
}
