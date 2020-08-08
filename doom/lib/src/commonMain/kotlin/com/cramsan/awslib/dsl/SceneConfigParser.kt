package com.cramsan.awslib.dsl

import com.cramsan.awslib.entity.implementation.Doctor
import com.cramsan.awslib.entity.implementation.Dog
import com.cramsan.awslib.entity.implementation.GameEntity
import com.cramsan.awslib.entity.implementation.Player
import com.cramsan.awslib.eventsystem.GameEntityTrigger
import com.cramsan.awslib.eventsystem.events.BaseEvent
import com.cramsan.awslib.eventsystem.events.ChangeTriggerEvent
import com.cramsan.awslib.eventsystem.events.InteractiveEvent
import com.cramsan.awslib.eventsystem.events.InteractiveEventOption
import com.cramsan.awslib.eventsystem.events.SwapEntityEvent
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

class EntityListBuilder {
    private val entityList = mutableListOf<GameEntity>()

    fun dog(block: DogBuilder.() -> Unit) {
        entityList.add(DogBuilder().apply(block).build())
    }

    fun scientist(block: ScientistBuilder.() -> Unit) {
        entityList.add(ScientistBuilder().apply(block).build())
    }

    internal fun build() = entityList
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

class GameEntityTriggerBuilder {
    var id = InitialValues.INVALID_ID
    var eventId = InitialValues.INVALID_ID
    var enabled = InitialValues.ENABLED_TRIGGER
    var targetId = InitialValues.INVALID_ID

    internal fun build() = GameEntityTrigger(
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

    fun entity(block: GameEntityTriggerBuilder.() -> Unit) {
        triggerList.add(GameEntityTriggerBuilder().apply(block).build())
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

class SwapEntityInteractiveEventBuilder {
    var id = InitialValues.INVALID_ID
    var nextEventId = InitialValues.INVALID_ID
    var enableEntityId = InitialValues.INVALID_ID
    var disableEntityId = InitialValues.INVALID_ID

    internal fun build() = SwapEntityEvent(
        id,
        nextEventId,
        enableEntityId,
        disableEntityId
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

    fun swapEntity(block: SwapEntityInteractiveEventBuilder.() -> Unit) {
        eventList.add(SwapEntityInteractiveEventBuilder().apply(block).build())
    }

    internal fun build() = eventList
}

class SceneConfigBuilder {
    private var player: Player? = null
    private val entityList = mutableListOf<GameEntity>()
    private val triggerList = mutableListOf<Trigger>()
    private val eventList = mutableListOf<BaseEvent>()

    fun player(block: PlayerBuilder.() -> Unit) {
        player = PlayerBuilder().apply(block).build()
    }

    fun entities(block: EntityListBuilder.() -> Unit) {
        entityList.addAll(EntityListBuilder().apply(block).build())
    }

    fun triggers(block: TriggerListBuilder.() -> Unit) {
        triggerList.addAll(TriggerListBuilder().apply(block).build())
    }

    fun events(block: EventListBuilder.() -> Unit) {
        eventList.addAll(EventListBuilder().apply(block).build())
    }

    internal fun build() = player?.let { SceneConfig(it, entityList, triggerList, eventList) }
}

/**
 * Top-level declaration for the scene configuration.
 */
fun scene(block: SceneConfigBuilder.() -> Unit): SceneConfig? = SceneConfigBuilder().apply(block).build()
