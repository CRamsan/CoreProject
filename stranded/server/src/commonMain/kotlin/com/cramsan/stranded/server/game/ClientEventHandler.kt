package com.cramsan.stranded.server.game

import com.cramsan.stranded.server.messages.ServerEvent

/**
 * Implement this interface on the client side to receive [ServerEvent].
 */
fun interface ClientEventHandler {

    /**
     * The [serverEvent] that was received by the client.
     */
    fun onServerEventReceived(serverEvent: ServerEvent)
}
