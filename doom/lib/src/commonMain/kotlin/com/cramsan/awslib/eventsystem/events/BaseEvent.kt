package com.cramsan.awslib.eventsystem.events

/**
 * A [BaseEvent] is an abstraction that will represent an action performed by the game engine.
 */
abstract class BaseEvent(
    val id: String,
    val type: EventType
) {
    override fun toString(): String {
        return "id: $id, type: $type"
    }
}
