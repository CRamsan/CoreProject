package com.cramsan.awslib.entity.implementation

import com.cramsan.awslib.enums.EntityType
import com.cramsan.awslib.utils.constants.InitialValues

/**
 * This class implements [com.cramsan.awslib.entity.implementation.Character] with defaults that
 * make it an enemy.
 */
open class Enemy(
    id: String,
    posX: Int,
    posY: Int,
    priority: Int,
    enabled: Boolean,
    health: Int,
    val enemyType: EnemyType,
    var range: Int,
    var damage: Double,
    var accuracy: Double,
    var move: Int,
    val vision: Int
) :
    Character(
        id,
        health,
        InitialValues.ENEMY_GROUP,
        posX,
        posY,
        EntityType.DOG,
        priority,
        enabled,
        true
    ) {

    override var attack = InitialValues.ATTACK_DOG
}
