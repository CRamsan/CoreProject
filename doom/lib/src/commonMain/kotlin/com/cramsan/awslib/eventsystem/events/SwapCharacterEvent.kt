package com.cramsan.awslib.eventsystem.events

class SwapCharacterEvent(
    id: String,
    nextEventId: String,
    enableEntityId: String,
    disableEntityId: String
) :
    NonInteractiveEvent(
        id,
        EventType.SWAPIDENTITY,
        nextEventId,
        enableEntityId,
        disableEntityId
    )
