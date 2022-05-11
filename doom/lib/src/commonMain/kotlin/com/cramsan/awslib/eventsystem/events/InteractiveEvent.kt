package com.cramsan.awslib.eventsystem.events

data class InteractiveEvent(
    override val id: String,
    val text: String,
    val options: List<InteractiveEventOption>,
) :
    BaseEvent(id)
