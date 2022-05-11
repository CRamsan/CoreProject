package com.cramsan.awslib.eventsystem.triggers

/**
 * A [Trigger] is a mechanism to initiate the [Event] with id [eventId]. A trigger may be enabled or disabled based on
 * the value of [enabled]. This class is abstract and should be extended to define how the [Event] will be triggered.
 */
abstract class Trigger(
    val id: String,
    val eventId: String,
    var enabled: Boolean,
) {
    override fun toString(): String {
        return "id: $id, event: $eventId, enabled: $enabled"
    }
}
