package com.cramsan.framework.userevents

/**
 * Module to record events around user engagement.
 */
interface UserEventsInterface {

    val platformDelegate: UserEventsDelegate

    /**
     * Initialize the module
     */
    fun initialize()

    /**
     * Record an instance of the [event] and [tag]
     */
    fun log(tag: String, event: String)

    /**
     * Record an instance of the [event] and [tag] with an included [metadata]
     */
    fun log(tag: String, event: String, metadata: Map<String, String>)
}
