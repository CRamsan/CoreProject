package me.cesar.application

import io.ktor.application.call
import io.ktor.html.respondHtml
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.request.receiveChannel
import io.ktor.request.receiveText
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.html.HTML
import me.cesar.application.enconding.Encoder
import me.cesar.application.me.Constants
import java.io.File

/**
 * Server that manages all endpoints for HTML pages and the encoding service.
 */
class Server(private val encoder: Encoder) {
    private val server = embeddedServer(Netty, port = Constants.PORT, host = Constants.HOST) {
        routing {
            /**
             * Needed for loading the js library
             */
            static("/static") {
                resources()
            }
            /**
             * Load the HTML site
             */
            get("/") {
                call.respondHtml(HttpStatusCode.OK, HTML::index)
            }
            /**
             * REST API endpoint
             */
            route(Constants.API_PATH) {
                post(Constants.API_ENCODE) {
                    val cipher = call.request.queryParameters["cipher"]
                    val seed = call.request.queryParameters["seed"]?.toInt()
                    val content = call.receiveText()

                    val channel = call.receiveChannel()
                    var byteArray = byteArrayOf()
                    while (!channel.isClosedForRead) {
                        byteArray += channel.readByte()
                    }
                    val file = File("Test")
                    file.writeBytes(byteArray)

                    println("Received request with content: $content")
                    val encodedContent = encoder.encode(content, cipher, seed)
                    println("Sending response with encoded content: $encodedContent")
                    call.respondText(encodedContent)
                }

                post(Constants.API_DECODE) {
                    val cipher = call.request.queryParameters["cipher"]
                    val seed = call.request.queryParameters["seed"]?.toInt()
                    val content = call.receiveText()
                    println("Received request with content: $content")
                    val encodedContent = encoder.decode(content, cipher, seed)
                    println("Sending response with decoded content: $encodedContent")
                    call.respondText(encodedContent)
                }
            }
        }
    }

    fun start() = server.start(wait = true)
}

/**
 URL : cramsan.com/api/encode
 METHOD POST
 Body: string to encode
 URL : cramsan.com/api/decode
 METHOD POST
 Body: string to decode
 */
