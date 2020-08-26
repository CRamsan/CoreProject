package com.cramsan.awslib.entity.implementation

import com.cramsan.awslib.entity.GameItemInterface

/**
 * Class that represents an item with an an attached event that gets triggered on contact.
 */
open class ConsumableItem(
    override val id: Int,
    override var posX: Int,
    override var posY: Int,
    var eventId: Int
) : GameItemInterface
