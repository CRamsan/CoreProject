package com.cramsan.awslib.eventsystem.events

/**
 * A [BaseEvent] is an abstraction that will represent an action performed by the game engine.
 */
abstract class BaseEvent(
    open val id: String,
)
