package com.cramsan.awslib.eventsystem.events

class InteractiveEventOption(
    val id: String,
    val eventId: String,
    val label: String
) {
    override fun toString(): String {
        return "id: $id, event: $eventId, label: $label"
    }
}
