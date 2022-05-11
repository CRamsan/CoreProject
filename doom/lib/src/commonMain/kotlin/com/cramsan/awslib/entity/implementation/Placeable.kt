package com.cramsan.awslib.entity.implementation

/**
 * This class implements [com.cramsan.awslib.entity.implementation.Character] with defaults that
 * make it a placeable entity that may be interacted with.
 */
open class Placeable(
    id: String,
    posX: Int,
    posY: Int,
    priority: Int,
    enabled: Boolean,
    health: Int,
    group: String,
    val placeableType: PlaceableType,
) :
    Character(
        id,
        health,
        group,
        posX,
        posY,
        priority,
        enabled,
        false,
    )
