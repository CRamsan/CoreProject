package com.cramsan.awslib.dsl

import com.cramsan.awslib.entity.CharacterInterface
import com.cramsan.awslib.entity.ItemInterface
import com.cramsan.awslib.entity.implementation.*
import com.cramsan.awslib.eventsystem.CharacterTrigger
import com.cramsan.awslib.eventsystem.events.BaseEvent
import com.cramsan.awslib.eventsystem.events.ChangeTriggerEvent
import com.cramsan.awslib.eventsystem.events.InteractiveEvent
import com.cramsan.awslib.eventsystem.events.InteractiveEventOption
import com.cramsan.awslib.eventsystem.events.SwapCharacterEvent
import com.cramsan.awslib.eventsystem.triggers.CellTrigger
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

class ConsumableItemBuilderBuilder {
    var id = InitialValues.INVALID_NAME
    var name = InitialValues.INVALID_NAME
    var type = ConsumableType.INVALID
    var ammount = InitialValues.INVALID_AMMOUNT

    internal fun build() = mapOf(name to ConsumableItemBuilder(
        id,
        name,
        type,
        ammount
    ))
}

class EquippableItemBuilderBuilder {
    var id = InitialValues.INVALID_NAME
    var name = InitialValues.INVALID_NAME
    var range = InitialValues.INVALID_AMMOUNT
    var accuracy: Double = InitialValues.INVALID_AMMOUNT.toDouble()
    var damage: Double = InitialValues.INVALID_AMMOUNT.toDouble()

    internal fun build() = mapOf(name to EquippableItemBuilder(
        id,
        name,
        range,
        accuracy,
        damage
    ))
}

class KeyItemBuilderBuilder {
    var id = InitialValues.INVALID_NAME
    var name = InitialValues.INVALID_NAME

    internal fun build() = mapOf(name to KeyItemBuilder(
        id,
        name
    ))
}

class EnemyBuilderBuilder {
    var id = InitialValues.INVALID_NAME
    var name = InitialValues.INVALID_NAME
    var type = EnemyType.INVALID
    var range = InitialValues.INVALID_AMMOUNT
    var damage: Double = InitialValues.INVALID_AMMOUNT.toDouble()
    var accuracy: Double = InitialValues.INVALID_AMMOUNT.toDouble()
    var move = InitialValues.INVALID_AMMOUNT

    internal fun build() = mapOf(name to EnemyBuilder(
        id,
        name,
        type,
        range,
        damage,
        accuracy,
        move
    ))
}

class AllyBuilderBuilder {
    var id = InitialValues.INVALID_NAME
    var name = InitialValues.INVALID_NAME
    var type = AllyType.INVALID

    internal fun build() = mapOf(name to AllyBuilder(
        id,
        name,
        type
    ))
}

class PlaceableBuilderBuilder {
    var id = InitialValues.INVALID_NAME
    var name = InitialValues.INVALID_NAME

    internal fun build() = mapOf(name to PlaceableBuilder(
        id,
        name,
    ))
}

class EntityInstanceBuilder(val entityBuilders: Map<String, EntityBuilder>) {
    var id = InitialValues.INVALID_ID
    var template = InitialValues.INVALID_NAME
    var posX = InitialValues.POS_X_ENTITY
    var posY = InitialValues.POS_Y_ENTITY
    var priority = InitialValues.PRIORITY_ENTITY
    var group = InitialValues.INVALID_ID
    var enabled = InitialValues.ENABLED_ENTITY

    internal fun build(): CharacterInterface {
        val builder = entityBuilders[template]
        return when (builder) {
            is AllyBuilder -> builder.build(id, posX, posY, priority, enabled)
            is EnemyBuilder -> builder.build(id, posX, posY, priority, enabled)
            is PlaceableBuilder -> builder.build(id, posX, posY, priority, enabled)
            else -> TODO()
        }
    }
}

class ItemInstanceBuilder(val itemBuilders: Map<String, EntityBuilder>) {
    var id = InitialValues.INVALID_ID
    var template = InitialValues.INVALID_NAME
    var posX = InitialValues.POS_X_ENTITY
    var posY = InitialValues.POS_Y_ENTITY

    internal fun build(): ItemInterface {
        val builder = itemBuilders[template]
        return when (builder) {
            is ConsumableItemBuilder -> builder.build(id, posX, posY)
            is EquippableItemBuilder -> builder.build(id, posX, posY)
            is KeyItemBuilder -> builder.build(id, posX, posY)
            else -> TODO()
        }
    }
}

class ItemBuildersBuilder {
    private val itemBuilderList = mutableMapOf<String, EntityBuilder>()

    fun consumable(block: ConsumableItemBuilderBuilder.() -> Unit) {
        itemBuilderList.putAll(ConsumableItemBuilderBuilder().apply(block).build())
    }

    fun equippable(block: EquippableItemBuilderBuilder.() -> Unit) {
        itemBuilderList.putAll(EquippableItemBuilderBuilder().apply(block).build())
    }

    fun keyItem(block: KeyItemBuilderBuilder.() -> Unit) {
        itemBuilderList.putAll(KeyItemBuilderBuilder().apply(block).build())
    }

    internal fun build() = itemBuilderList
}

class EntityBuildersBuilder {
    private val entityBuilderList = mutableMapOf<String, EntityBuilder>()

    fun enemy(block: EnemyBuilderBuilder.() -> Unit) {
        entityBuilderList.putAll(EnemyBuilderBuilder().apply(block).build())
    }

    fun ally(block: AllyBuilderBuilder.() -> Unit) {
        entityBuilderList.putAll(AllyBuilderBuilder().apply(block).build())
    }

    fun placeable(block: PlaceableBuilderBuilder.() -> Unit) {
        entityBuilderList.putAll(PlaceableBuilderBuilder().apply(block).build())
    }

    internal fun build() = entityBuilderList
}

class EntityListBuilder(val entityBuilders: Map<String, EntityBuilder>) {
    private val entityList = mutableListOf<CharacterInterface>()

    fun entity(block: EntityInstanceBuilder.() -> Unit) {
        entityList.add(EntityInstanceBuilder(entityBuilders).apply(block).build())
    }

    internal fun build() = entityList
}

class ItemListBuilder(val itemBuilders: Map<String, EntityBuilder>) {
    private val itemList = mutableListOf<ItemInterface>()

    fun item(block: ItemInstanceBuilder.() -> Unit) {
        itemList.add(ItemInstanceBuilder(itemBuilders).apply(block).build())
    }

    internal fun build() = itemList
}

class CellTriggerBuilder {
    var id = InitialValues.INVALID_ID
    var eventId = InitialValues.INVALID_ID
    var enabled = InitialValues.ENABLED_TRIGGER
    var posX = InitialValues.POS_X_TRIGGER
    var posY = InitialValues.POS_Y_TRIGGER

    internal fun build() = CellTrigger(
        id,
        eventId,
        enabled,
        posX,
        posY
    )
}

class CharacterTriggerBuilder {
    var id = InitialValues.INVALID_ID
    var eventId = InitialValues.INVALID_ID
    var enabled = InitialValues.ENABLED_TRIGGER
    var targetId = InitialValues.INVALID_ID

    internal fun build() = CharacterTrigger(
        id,
        eventId,
        enabled,
        targetId
    )
}

class TriggerListBuilder {
    private val triggerList = mutableListOf<Trigger>()

    fun cell(block: CellTriggerBuilder.() -> Unit) {
        triggerList.add(CellTriggerBuilder().apply(block).build())
    }

    fun character(block: CharacterTriggerBuilder.() -> Unit) {
        triggerList.add(CharacterTriggerBuilder().apply(block).build())
    }

    internal fun build() = triggerList
}

class InteractiveEventOptionBuilder {
    var id = InitialValues.INVALID_ID
    var eventId = InitialValues.INVALID_ID
    var label = InitialValues.INVALID_LABEL_OPTION

    internal fun build() = InteractiveEventOption(
        id,
        eventId,
        label
    )
}

class InteractiveEventBuilder {
    var id = InitialValues.INVALID_ID
    var text = InitialValues.INVALID_TEXT_EVENT

    private val options = mutableListOf<InteractiveEventOption>()

    fun option(block: InteractiveEventOptionBuilder.() -> Unit) {
        options.add(InteractiveEventOptionBuilder().apply(block).build())
    }

    internal fun build() = InteractiveEvent(id, text, options)
}

class ChangeTriggerEventBuilder {
    var id = InitialValues.INVALID_ID
    var nextEventId = InitialValues.INVALID_ID
    var enableEventId = InitialValues.INVALID_ID
    var disableEventId = InitialValues.INVALID_ID

    internal fun build() = ChangeTriggerEvent(
        id,
        nextEventId,
        enableEventId,
        disableEventId
    )
}

class SwapCharacterInteractiveEventBuilder {
    var id = InitialValues.INVALID_ID
    var nextEventId = InitialValues.INVALID_ID
    var enableCharacterId = InitialValues.INVALID_ID
    var disableCharacterId = InitialValues.INVALID_ID

    internal fun build() = SwapCharacterEvent(
        id,
        nextEventId,
        enableCharacterId,
        disableCharacterId
    )
}

class EventListBuilder {
    private val eventList = mutableListOf<BaseEvent>()

    fun interactive(block: InteractiveEventBuilder.() -> Unit) {
        eventList.add(InteractiveEventBuilder().apply(block).build())
    }

    fun changeTrigger(block: ChangeTriggerEventBuilder.() -> Unit) {
        eventList.add(ChangeTriggerEventBuilder().apply(block).build())
    }

    fun swapCharacter(block: SwapCharacterInteractiveEventBuilder.() -> Unit) {
        eventList.add(SwapCharacterInteractiveEventBuilder().apply(block).build())
    }

    internal fun build() = eventList
}

class SceneConfigBuilder {
    private var player: Player? = null
    private val itemBuilders = mutableMapOf<String, EntityBuilder>()
    private val entityBuilders = mutableMapOf<String, EntityBuilder>()
    private val characterList = mutableListOf<CharacterInterface>()
    private val itemList = mutableListOf<ItemInterface>()
    private val triggerList = mutableListOf<Trigger>()
    private val eventList = mutableListOf<BaseEvent>()

    fun player(block: PlayerBuilder.() -> Unit) {
        player = PlayerBuilder().apply(block).build()
    }

    fun itemBuilders(block: ItemBuildersBuilder.() -> Unit) {
        itemBuilders.putAll(ItemBuildersBuilder().apply(block).build())
    }

    fun entityBuilders(block: EntityBuildersBuilder.() -> Unit) {
        entityBuilders.putAll(EntityBuildersBuilder().apply(block).build())
    }

    fun items(block: ItemListBuilder.() -> Unit) {
        itemList.addAll(ItemListBuilder(itemBuilders).apply(block).build())
    }

    fun entity(block: EntityListBuilder.() -> Unit) {
        characterList.addAll(EntityListBuilder(entityBuilders).apply(block).build())
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
