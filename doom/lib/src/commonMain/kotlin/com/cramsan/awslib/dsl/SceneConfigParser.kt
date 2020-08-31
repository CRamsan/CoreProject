package com.cramsan.awslib.dsl

import com.cramsan.awslib.entity.CharacterInterface
import com.cramsan.awslib.entity.ItemInterface
import com.cramsan.awslib.entity.implementation.ConsumableItem
import com.cramsan.awslib.entity.implementation.ConsumableType
import com.cramsan.awslib.entity.implementation.Doctor
import com.cramsan.awslib.entity.implementation.Dog
import com.cramsan.awslib.entity.implementation.EquippableItem
import com.cramsan.awslib.entity.implementation.KeyItem
import com.cramsan.awslib.entity.implementation.Player
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

class DogBuilder {
    var id = InitialValues.INVALID_ID
    var posX = InitialValues.POS_X_ENTITY
    var posY = InitialValues.POS_Y_ENTITY
    var priority = InitialValues.PRIORITY_ENTITY
    var enabled = InitialValues.ENABLED_ENTITY

    internal fun build() = Dog(
        id,
        posX,
        posY,
        priority,
        enabled
    )
}

class ScientistBuilder {
    var id = InitialValues.INVALID_ID
    var posX = InitialValues.POS_X_ENTITY
    var posY = InitialValues.POS_Y_ENTITY
    var priority = InitialValues.PRIORITY_ENTITY
    var group = InitialValues.INVALID_ID
    var enabled = InitialValues.ENABLED_ENTITY

    internal fun build() = Doctor(
        id,
        group,
        posX,
        posY,
        priority,
        enabled
    )
}

class HealthBuilder {
    var id = InitialValues.INVALID_ID
    var posX = InitialValues.POS_X_ENTITY
    var posY = InitialValues.POS_Y_ENTITY
    var eventId = InitialValues.NOOP_ID

    internal fun build() = ConsumableItem(
        id,
        posX,
        posY,
        ConsumableType.HEALTH,
        eventId
    )
}

class KeyCardBuilder {
    var id = InitialValues.INVALID_ID
    var posX = InitialValues.POS_X_ENTITY
    var posY = InitialValues.POS_Y_ENTITY

    internal fun build() = KeyItem(
        id,
        posX,
        posY
    )
}

class GunBuilder {
    var id = InitialValues.INVALID_ID
    var posX = InitialValues.POS_X_ENTITY
    var posY = InitialValues.POS_Y_ENTITY

    internal fun build() = EquippableItem(
        id,
        posX,
        posY
    )
}

class CharacterListBuilder {
    private val characterList = mutableListOf<CharacterInterface>()

    fun dog(block: DogBuilder.() -> Unit) {
        characterList.add(DogBuilder().apply(block).build())
    }

    fun scientist(block: ScientistBuilder.() -> Unit) {
        characterList.add(ScientistBuilder().apply(block).build())
    }

    internal fun build() = characterList
}

class ItemListBuilder {
    private val itemList = mutableListOf<ItemInterface>()

    fun health(block: HealthBuilder.() -> Unit) {
        itemList.add(HealthBuilder().apply(block).build())
    }

    fun keycard(block: KeyCardBuilder.() -> Unit) {
        itemList.add(KeyCardBuilder().apply(block).build())
    }

    fun gun(block: GunBuilder.() -> Unit) {
        itemList.add(GunBuilder().apply(block).build())
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
    private val characterList = mutableListOf<CharacterInterface>()
    private val itemList = mutableListOf<ItemInterface>()
    private val triggerList = mutableListOf<Trigger>()
    private val eventList = mutableListOf<BaseEvent>()

    fun player(block: PlayerBuilder.() -> Unit) {
        player = PlayerBuilder().apply(block).build()
    }

    fun characters(block: CharacterListBuilder.() -> Unit) {
        characterList.addAll(CharacterListBuilder().apply(block).build())
    }

    fun items(block: ItemListBuilder.() -> Unit) {
        itemList.addAll(ItemListBuilder().apply(block).build())
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
