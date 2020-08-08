package com.cramsan.awslib.eventsystem.events

class ChangeTriggerEvent(
    id: Int,
    nextEventId: Int,
    enableEventId: Int,
    disableEventId: Int
) :
    NonInteractiveEvent(
        id,
        EventType.CHANGETRIGGER,
        nextEventId,
        disableEventId,
        enableEventId
    )
