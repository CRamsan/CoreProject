package com.cramsan.awslib.eventsystem.triggers

/**
 * Implementation of a [Trigger] that will trigger an [Event] based the interation with an [GameEntityInterface] with id
 * [targetId]
 */
class CharacterTrigger(
    id: String,
    eventId: String,
    enabled: Boolean,
    val targetId: String,
) :
    Trigger(id, eventId, enabled)
