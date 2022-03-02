package me.cesar.application

import io.ktor.application.call
import io.ktor.html.respondHtml
import io.ktor.http.HttpStatusCode
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.request.receive
import io.ktor.request.receiveText
import io.ktor.routing.post
import kotlinx.html.*
import me.cesar.application.enconding.Base64Encoder

fun main() {
    val encoder = Base64Encoder()
    val server = Server(encoder)
    server.start()
}