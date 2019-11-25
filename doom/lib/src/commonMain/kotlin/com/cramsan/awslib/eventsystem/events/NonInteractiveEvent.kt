package com.cramsan.awslib.eventsystem.events

abstract class NonInteractiveEvent(
    id: Int,
    type: EventType,
    val nextEventId: Int,
    val enableId: Int,
    val disableId: Int
) :
        BaseEvent(id, type)