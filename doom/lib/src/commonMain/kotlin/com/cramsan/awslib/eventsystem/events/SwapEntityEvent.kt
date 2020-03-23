package com.cramsan.awslib.eventsystem.events

class SwapEntityEvent(
    id: Int,
    nextEventId: Int,
    enableEntityId: Int,
    disableEntityId: Int
) :
        NonInteractiveEvent(id,
                EventType.CHANGETRIGGER,
                nextEventId,
                enableEntityId,
                disableEntityId)
