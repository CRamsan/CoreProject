package com.cramsan.awslib.eventsystem.events

abstract class BaseEvent(
    val id: Int,
    val type: EventType
) {
    override fun toString(): String {
        return "id: $id, type: $type"
    }
}
