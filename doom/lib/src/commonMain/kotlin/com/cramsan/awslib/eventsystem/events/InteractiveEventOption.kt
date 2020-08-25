package com.cramsan.awslib.eventsystem.events

class InteractiveEventOption(
    val id: Int,
    val eventId: Int,
    val label: String
) {
    override fun toString(): String {
        return "id: $id, event: $eventId, label: $label"
    }
}
