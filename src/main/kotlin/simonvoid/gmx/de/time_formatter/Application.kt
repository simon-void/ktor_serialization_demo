package simonvoid.gmx.de.time_formatter

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import simonvoid.gmx.de.time_formatter.plugins.configureRouting
import simonvoid.gmx.de.time_formatter.plugins.configureSerialization


fun main() {
    embeddedServer(Netty, port = 8080, host = "localhost") {
        configureSerialization()
        configureRouting()
    }.start(wait = true)
}