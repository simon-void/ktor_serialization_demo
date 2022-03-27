package simonvoid.gmx.de.time_formatter.plugins

import io.ktor.serialization.*
import io.ktor.features.*
import io.ktor.application.*
import kotlinx.serialization.json.Json

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }
}
