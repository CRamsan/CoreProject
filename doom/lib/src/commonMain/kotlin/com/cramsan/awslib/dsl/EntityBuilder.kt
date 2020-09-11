package com.cramsan.awslib.dsl

import com.cramsan.awslib.entity.implementation.Ally
import com.cramsan.awslib.entity.implementation.ConsumableItem
import com.cramsan.awslib.entity.implementation.ConsumableType
import com.cramsan.awslib.entity.implementation.Enemy

/**
 * Class that will build a [GameEntity]
 */
abstract class EntityBuilder(
    val id: String,
    val name: String
) {

}