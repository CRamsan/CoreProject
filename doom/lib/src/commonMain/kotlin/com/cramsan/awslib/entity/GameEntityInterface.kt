package com.cramsan.awslib.entity

import com.cramsan.awslib.map.GridPositionableInterface

/**
 * This interface defines a contract for elements that are displayed in the [com.cramsan.awslib.map.GameMap] and that
 * can be interacted on.
 */
interface GameEntityInterface : GridPositionableInterface {
    val id: Int
}
