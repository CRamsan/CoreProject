package com.cramsan.awslib.eventsystem

import com.cramsan.awslib.eventsystem.triggers.Trigger

class GameEntityTrigger(
    id: Int,
    eventId: Int,
    enabled: Boolean,
    val targetId: Int
) :
        Trigger(id, eventId, enabled)