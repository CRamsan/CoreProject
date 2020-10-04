package com.cramsan.awslib.dsl

import com.cramsan.awslib.dsl.builders.EntityBuilder
import com.cramsan.awslib.entity.CharacterInterface
import com.cramsan.awslib.entity.ItemInterface
import com.cramsan.awslib.entity.implementation.Player
import com.cramsan.awslib.eventsystem.events.BaseEvent
import com.cramsan.awslib.eventsystem.triggers.Trigger
import com.cramsan.awslib.scene.SceneConfig
import com.cramsan.awslib.utils.constants.InitialValues

/**
 * Top level functions that create a DSL for defining a game scene
 */

class PlayerBuilder {
    var posX = InitialValues.POS_X_ENTITY
    var posY = InitialValues.POS_Y_ENTITY
    var speed = InitialValues.SPEED_PLAYER

    internal fun build() = Player(
        posX,
        posY,
        speed
    )
}

class SceneConfigBuilder {
    private var player: Player? = null
    private val consumableBuilders = mutableMapOf<String, EntityBuilder>()
    private val equippableBuilders = mutableMapOf<String, EntityBuilder>()
    private val keyItemBuilders = mutableMapOf<String, EntityBuilder>()
    private val allyBuilders = mutableMapOf<String, EntityBuilder>()
    private val enemyBuilders = mutableMapOf<String, EntityBuilder>()
    private val placeableBuilders = mutableMapOf<String, EntityBuilder>()
    private val characterList = mutableListOf<CharacterInterface>()
    private val itemList = mutableListOf<ItemInterface>()
    private val triggerList = mutableListOf<Trigger>()
    private val eventList = mutableListOf<BaseEvent>()

    fun player(block: PlayerBuilder.() -> Unit) {
        player = PlayerBuilder().apply(block).build()
    }

    fun itemBuilders(block: ItemBuildersBuilder.() -> Unit) {
        consumableBuilders.putAll(ItemBuildersBuilder().apply(block).buildConsumableItems())
        equippableBuilders.putAll(ItemBuildersBuilder().apply(block).buildEquippableItems())
        keyItemBuilders.putAll(ItemBuildersBuilder().apply(block).buildKeyItems())
    }

    fun entityBuilders(block: EntityBuildersBuilder.() -> Unit) {
        enemyBuilders.putAll(EntityBuildersBuilder().apply(block).buildEnemy())
        allyBuilders.putAll(EntityBuildersBuilder().apply(block).buildAlly())
        placeableBuilders.putAll(EntityBuildersBuilder().apply(block).buildPlaceable())
    }

    fun items(block: ItemListBuilder.() -> Unit) {
        itemList.addAll(ItemListBuilder(consumableBuilders, equippableBuilders, keyItemBuilders).apply(block).build())
    }

    fun entity(block: EntityListBuilder.() -> Unit) {
        characterList.addAll(EntityListBuilder(enemyBuilders, allyBuilders, placeableBuilders).apply(block).build())
    }

    fun triggers(block: TriggerListBuilder.() -> Unit) {
        triggerList.addAll(TriggerListBuilder().apply(block).build())
    }

    fun events(block: EventListBuilder.() -> Unit) {
        eventList.addAll(EventListBuilder().apply(block).build())
    }

    internal fun build() = player?.let { SceneConfig(it, characterList, itemList, triggerList, eventList) }
}

/**
 * Top-level declaration for the scene configuration.
 */
fun scene(block: SceneConfigBuilder.() -> Unit): SceneConfig? = SceneConfigBuilder().apply(block).build()
