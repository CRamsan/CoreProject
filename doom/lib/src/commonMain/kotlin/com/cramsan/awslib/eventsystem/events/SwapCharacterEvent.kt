package com.cramsan.awslib.eventsystem.events

data class SwapCharacterEvent(
    override val id: String,
    override val nextEventId: String,
    override val enableId: String,
    override val disableId: String,
) :
    NonInteractiveEvent(
        id,
        nextEventId,
        enableId,
        disableId,
    )
