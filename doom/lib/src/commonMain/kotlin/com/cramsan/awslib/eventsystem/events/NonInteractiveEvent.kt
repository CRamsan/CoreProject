package com.cramsan.awslib.eventsystem.events

abstract class NonInteractiveEvent(
    id: String,
    type: EventType,
    val nextEventId: String,
    val enableId: String,
    val disableId: String
) :
    BaseEvent(id, type)
