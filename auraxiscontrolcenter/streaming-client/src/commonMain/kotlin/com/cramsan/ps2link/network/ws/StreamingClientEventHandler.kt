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
}
