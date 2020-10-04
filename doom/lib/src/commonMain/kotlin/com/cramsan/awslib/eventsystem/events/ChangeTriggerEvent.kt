package com.cramsan.awslib.eventsystem.events

/**
 *
 */
class ChangeTriggerEvent(
    id: String,
    nextEventId: String,
    enableEventId: String,
    disableEventId: String
) :
    NonInteractiveEvent(
        id,
        EventType.CHANGETRIGGER,
        nextEventId,
        disableEventId,
        enableEventId
    )
