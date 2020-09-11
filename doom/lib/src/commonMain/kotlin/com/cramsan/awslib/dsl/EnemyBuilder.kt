package com.cramsan.awslib.dsl

import com.cramsan.awslib.entity.implementation.*
import com.cramsan.awslib.utils.constants.InitialValues

/**
 * Class that will build a [ConsumableItem]
 */
class EnemyBuilder(
    id: String,
    name: String,
    val enemyType: EnemyType,
    var range: Int,
    var damage: Double,
    var accuracy: Double,
    var move: Int
) : EntityBuilder(id, name) {

    fun build(entityId: Int,
              posX: Int,
              posY: Int,
              priority: Int,
              enabled: Boolean): Enemy {
        return Enemy(entityId, posX, posY, priority, enabled)
    }
}