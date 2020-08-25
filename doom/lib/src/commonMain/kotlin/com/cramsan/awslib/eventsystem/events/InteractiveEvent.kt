package com.cramsan.awslib.eventsystem.events

class InteractiveEvent(
    id: Int,
    val text: String,
    val options: List<InteractiveEventOption>
) :
    BaseEvent(id, EventType.INTERACTION) {

    override fun toString(): String {
        return "${super.toString()}, options: ${options.size}"
    }
}
