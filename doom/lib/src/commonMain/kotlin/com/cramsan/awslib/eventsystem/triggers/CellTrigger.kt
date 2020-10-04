package com.cramsan.awslib.eventsystem.triggers

/**
 * Implementation of a [Trigger] that will trigger an [Event] based on the [posX] and [posY] location in a map.
 */
class CellTrigger(
    id: String,
    eventId: String,
    enabled: Boolean,
    val posX: Int,
    val posY: Int
) :
    Trigger(id, eventId, enabled)
