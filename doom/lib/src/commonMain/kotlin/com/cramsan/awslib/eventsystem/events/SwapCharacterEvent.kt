package com.cramsan.awslib.eventsystem.events

class SwapCharacterEvent(
    id: Int,
    nextEventId: Int,
    enableEntityId: Int,
    disableEntityId: Int
) :
    NonInteractiveEvent(
        id,
        EventType.SWAPIDENTITY,
        nextEventId,
        enableEntityId,
        disableEntityId
    )
