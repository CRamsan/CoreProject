package com.cramsan.awslib.dsl

import com.cramsan.awslib.dsl.builders.AllyBuilder
import com.cramsan.awslib.dsl.builders.EnemyBuilder
import com.cramsan.awslib.dsl.builders.EntityBuilder
import com.cramsan.awslib.dsl.builders.PlaceableBuilder
import com.cramsan.awslib.entity.CharacterInterface
import com.cramsan.awslib.entity.implementation.AllyType
import com.cramsan.awslib.entity.implementation.EnemyType
import com.cramsan.awslib.entity.implementation.PlaceableType
import com.cramsan.awslib.utils.constants.InitialValues
import com.cramsan.awslib.utils.constants.InitialValues.ENEMY_GROUP
import com.cramsan.awslib.utils.constants.InitialValues.GROUP_PLAYER
import com.cramsan.awslib.utils.constants.InitialValues.INVALID_AMOUNT
import com.cramsan.awslib.utils.constants.InitialValues.PLACEABLE_HEALTH

class EnemyBuilderBuilder {
    var id = InitialValues.INVALID_ID
    var name = InitialValues.INVALID_ID
    var type = EnemyType.INVALID
    var range = INVALID_AMOUNT
    var damage: Double = INVALID_AMOUNT.toDouble()
    var accuracy: Double = INVALID_AMOUNT.toDouble()
    var move = INVALID_AMOUNT
    var health = INVALID_AMOUNT
    var vision = INVALID_AMOUNT
    var priority = InitialValues.PRIORITY_ENTITY
    var enable = true

    internal fun build() = mapOf(
        id to EnemyBuilder(
            id,
            type,
            range,
            damage,
            accuracy,
            move,
            vision,
            health,
            priority,
            enable,
        ),
    )
}

class AllyBuilderBuilder {
    var id = InitialValues.INVALID_ID
    var name = InitialValues.INVALID_ID
    var health = INVALID_AMOUNT
    var type = AllyType.INVALID
    var priority = InitialValues.PRIORITY_ENTITY
    var enable = true

    internal fun build() = mapOf(
        id to AllyBuilder(
            id,
            health,
            type,
            priority,
            enable,
        ),
    )
}

class PlaceableBuilderBuilder {
    var id = InitialValues.INVALID_ID
    var name = InitialValues.INVALID_ID
    var destructible = false
    var health = PLACEABLE_HEALTH
    var priority = InitialValues.PRIORITY_ENTITY
    var enabled = true
    var type = PlaceableType.INVALID

    internal fun build(): Map<String, PlaceableBuilder> {
        val group = if (destructible) GROUP_PLAYER else ENEMY_GROUP
        return mapOf(
            id to PlaceableBuilder(
                id,
                health,
                priority,
                enabled,
                group,
                type,
            ),
        )
    }
}

class EntityInstanceBuilder(val entityBuilders: Map<String, EntityBuilder>) {
    var id = InitialValues.INVALID_ID
    var template = InitialValues.INVALID_ID
    var posX = InitialValues.POS_X_ENTITY
    var posY = InitialValues.POS_Y_ENTITY
    var priority = InitialValues.PRIORITY_ENTITY
    var group = InitialValues.INVALID_ID
    var enabled = InitialValues.ENABLED_ENTITY
    var health = InitialValues.INVALID_AMOUNT

    internal fun build(): CharacterInterface {
        if (template == InitialValues.INVALID_ID) {
            throw RuntimeException("Template for entity with ID $id is not defined")
        }

        val builder = entityBuilders[template]
            ?: throw RuntimeException("Could not find template for $template")

        return when (builder) {
            is AllyBuilder -> builder.build(id, posX, posY, priority, enabled)
            is EnemyBuilder -> builder.build(id, posX, posY, priority, enabled)
            is PlaceableBuilder -> builder.build(id, posX, posY, priority, enabled, health, group)
            else -> TODO()
        }
    }
}

class EntityBuildersBuilder {
    private val enemyBuilderList = mutableMapOf<String, EntityBuilder>()
    private val allyBuilderList = mutableMapOf<String, EntityBuilder>()
    private val placeableBuilderList = mutableMapOf<String, EntityBuilder>()

    fun enemy(block: EnemyBuilderBuilder.() -> Unit) {
        enemyBuilderList.putAll(EnemyBuilderBuilder().apply(block).build())
    }

    fun ally(block: AllyBuilderBuilder.() -> Unit) {
        allyBuilderList.putAll(AllyBuilderBuilder().apply(block).build())
    }

    fun placeable(block: PlaceableBuilderBuilder.() -> Unit) {
        placeableBuilderList.putAll(PlaceableBuilderBuilder().apply(block).build())
    }

    internal fun buildEnemy() = enemyBuilderList
    internal fun buildAlly() = allyBuilderList
    internal fun buildPlaceable() = placeableBuilderList
}

class EntityListBuilder(
    val enemyBuilders: Map<String, EntityBuilder>,
    val allyBuilders: Map<String, EntityBuilder>,
    val placeableBuilders: Map<String, EntityBuilder>,
) {
    private val entityList = mutableListOf<CharacterInterface>()

    fun enemy(block: EntityInstanceBuilder.() -> Unit) {
        entityList.add(EntityInstanceBuilder(enemyBuilders).apply(block).build())
    }

    fun ally(block: EntityInstanceBuilder.() -> Unit) {
        entityList.add(EntityInstanceBuilder(allyBuilders).apply(block).build())
    }

    fun placeable(block: EntityInstanceBuilder.() -> Unit) {
        entityList.add(EntityInstanceBuilder(placeableBuilders).apply(block).build())
    }

    internal fun build() = entityList
}
