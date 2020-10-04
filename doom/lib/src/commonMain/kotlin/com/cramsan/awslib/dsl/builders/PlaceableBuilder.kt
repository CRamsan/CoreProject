package com.cramsan.awslib.dsl.builders

import com.cramsan.awslib.entity.implementation.GameEntity
import com.cramsan.awslib.entity.implementation.Placeable
import com.cramsan.awslib.entity.implementation.PlaceableType

/**
 * Class that will build an entity of type [Placeable]
 */
class PlaceableBuilder(
    templateName: String,
    private val health: Int,
    private val priority: Int,
    private val enabled: Boolean,
    private val group: String,
    private val placeableType: PlaceableType,
) : EntityBuilder(templateName) {

    fun build(
        entityId: String,
        posX: Int,
        posY: Int,
        priority: Int?,
        enabled: Boolean?,
        health: Int?,
        group: String?,
    ): GameEntity {
        return Placeable(
            entityId,
            posX,
            posY,
            priority ?: this.priority,
            enabled ?: this.enabled,
            health ?: this.health,
            group ?: this.group,
            placeableType
        )
    }
}
