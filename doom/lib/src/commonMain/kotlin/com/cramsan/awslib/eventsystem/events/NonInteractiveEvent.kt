package com.cramsan.awslib.eventsystem.events

abstract class NonInteractiveEvent(
    id: String,
    open val nextEventId: String,
    open val enableId: String,
    open val disableId: String,
) :
    BaseEvent(id)
