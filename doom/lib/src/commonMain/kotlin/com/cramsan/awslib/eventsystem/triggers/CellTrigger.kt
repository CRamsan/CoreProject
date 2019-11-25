package com.cramsan.awslib.eventsystem.triggers

class CellTrigger(
    id: Int,
    eventId: Int,
    enabled: Boolean,
    val posX: Int,
    val posY: Int
) :
        Trigger(id, eventId, enabled)