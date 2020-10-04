package com.cramsan.awslib.dsl

import com.cramsan.awslib.eventsystem.events.BaseEvent
import com.cramsan.awslib.eventsystem.events.ChangeTriggerEvent
import com.cramsan.awslib.eventsystem.events.InteractiveEvent
import com.cramsan.awslib.eventsystem.events.InteractiveEventOption
import com.cramsan.awslib.eventsystem.events.SwapCharacterEvent
import com.cramsan.awslib.utils.constants.InitialValues

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
