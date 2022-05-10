package simonvoid.gmx.de.time_formatter.plugins

import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import simonvoid.gmx.de.time_formatter.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Ktor Serialisation Demo")
        }
        post("/format") {
            val incomingOpeningTimes = call.receive<Map<String, List<IncomingOpeningTime>>>()
            // the first transformation replaces als Strings ("monday"-"sunday, "open"/"close") with Enums and sealed Classes
            // to make sure no write error was contained in the incomming data
            val eventList: List<StatusEvent> = incomingOpeningTimes.toStatusEvents()
            // after further checks (is each day at most present once, is the closing time less than 24h after the opening time)
            // the intermediate representation is mapped to the final representation which is a Kotlin representation
            // of the expected format
            val openPeriodsByDay: List<Pair<Weekday, List<OpenPeriod>>> = eventList.toOpenPeriodsByDay()

            // return a string representation of the final representation
            call.respondText(openPeriodsByDay.prettyPrint(), status = HttpStatusCode.OK)
        }
    }
}