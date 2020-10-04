package com.cramsan.awslib.dsl.builders

import com.cramsan.awslib.entity.implementation.Enemy
import com.cramsan.awslib.entity.implementation.EnemyType

/**
 * Class that will build an entity of type [Enemy]
 */
class EnemyBuilder(
    templateName: String,
    private val enemyType: EnemyType,
    private var range: Int,
    private var damage: Double,
    private var accuracy: Double,
    private var move: Int,
    private var vision: Int,
    private val health: Int,
    private val priority: Int,
    private val enabled: Boolean,
) : EntityBuilder(templateName) {

    fun build(
        entityId: String,
        posX: Int,
        posY: Int,
        priority: Int?,
        enabled: Boolean?,
    ): Enemy {
        return Enemy(
            entityId,
            posX,
            posY,
            priority ?: this.priority,
            enabled ?: this.enabled,
            health,
            enemyType,
            range,
            damage,
            accuracy,
            move,
            vision
        )
    }
}
