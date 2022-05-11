package com.cramsan.awslib.eventsystem.events

/**
 *
 */
data class ChangeTriggerEvent(
    override val id: String,
    override val nextEventId: String,
    val enableEventId: String,
    val disableEventId: String,
) :
    NonInteractiveEvent(
        id,
        nextEventId,
        disableEventId,
        enableEventId,
    )
