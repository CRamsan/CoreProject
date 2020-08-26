package com.cramsan.awslib.entity.implementation

import com.cramsan.awslib.entity.GameItemInterface

/**
 * Class that represents an item that is picked up and stored as part of the player inventory.
 */
class KeyItem(
    override val id: Int,
    override var posX: Int,
    override var posY: Int,
) : GameItemInterface
