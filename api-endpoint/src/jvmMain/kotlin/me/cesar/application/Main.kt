package me.cesar.application

import me.cesar.application.enconding.Base64Encoder

/**
 * Program entry-point.
 */
fun main() {
    val encoder = Base64Encoder()
    val server = Server(encoder)
    server.start()
}
