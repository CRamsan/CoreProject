package com.cramsan.awslib.dsl.builders

import com.cramsan.awslib.entity.implementation.Ally
import com.cramsan.awslib.entity.implementation.AllyType

/**
 * Class that will build an entity of type [Ally]
 */
class AllyBuilder(
    templateName: String,
    private val health: Int,
    private val allyType: AllyType,
    private val priority: Int,
    private val enabled: Boolean,
) : EntityBuilder(templateName) {

    fun build(
        entityId: String,
        posX: Int,
        posY: Int,
        priority: Int?,
        enabled: Boolean?,
    ): Ally {
        return Ally(
            entityId,
            health,
            posX,
            posY,
            priority ?: this.priority,
            enabled ?: this.enabled,
            allyType,
        )
    }
}
