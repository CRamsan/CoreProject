package com.cramsan.ps2link.network.ws

import com.cramsan.ps2link.network.ws.messages.ServerEvent

/**
 * Implement this interface to receive events from the [StreamingClient].
 */
interface StreamingClientEventHandler {

    /**
     * Message was received and it was correctly parsed into a [ServerEvent].
     */
    fun onServerEventReceived(serverEvent: ServerEvent)

    /**
     * [serverEventString] could not be parsed into a [ServerEvent] so it is provided as a raw string.
     */
    fun onUnhandledServerEventReceived(serverEventString: String)
}
