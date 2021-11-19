package com.cramsan.stranded.lib

import io.ktor.http.cio.websocket.DefaultWebSocketSession

class Connection(
    val playerId: String,
    val session: DefaultWebSocketSession
)
