package com.cramsan.awslib.eventsystem.triggers

abstract class Trigger(
    val id: Int,
    val eventId: Int,
    var enabled: Boolean
) {
    override fun toString(): String {
        return "id: $id, event: $eventId, enabled: $enabled"
    }
}
