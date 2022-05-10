package simonvoid.gmx.de.time_formatter

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlin.test.*
import io.ktor.server.testing.*
import simonvoid.gmx.de.time_formatter.plugins.configureRouting
import simonvoid.gmx.de.time_formatter.plugins.configureSerialization

class ApplicationTest {

    @Test
    fun testRoot() = testApplication {
        application {
            configureSerialization()
            configureRouting()
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Ktor Serialisation Demo", bodyAsText())
        }
    }

    @Test
    fun testConversion() = testApplication {
        application {
            configureSerialization()
            configureRouting()
        }
        client.post("/format") {
            headers.append("Content-Type", "application/json;charset=UTF-8")
            setBody(
                """
                    {
                    "monday" : [],
                    "tuesday" : [
                        {
                        "type" : "open",
                        "value" : 36000
                        },
                        {
                        "type" : "close",
                        "value" : 37800
                        },
                        {
                        "type" : "open",
                        "value" : 43200
                        },
                        {
                        "type" : "close",
                        "value" : 64800
                        }
                    ],
                    "wednesday" : [],
                    "thursday" : [
                        {
                        "type" : "open",
                        "value" : 37800
                        },
                        {
                        "type" : "close",
                        "value" : 64800
                        }
                    ],
                    "friday" : [
                        {
                        "type" : "open",
                        "value" : 36000
                        }
                    ],
                    "saturday" : [
                        {
                        "type" : "close",
                        "value" : 3600
                        },
                        {
                        "type" : "open",
                        "value" : 36000
                        }
                    ],
                    "sunday" : [
                        {
                        "type" : "close",
                        "value" : 3600
                        },
                        {
                        "type" : "open",
                        "value" : 43200
                        },
                        {
                        "type" : "close",
                        "value" : 75600
                        }
                    ]
                    }
                    """.trimIndent()
            )
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(
                """
                    Monday: Closed
                    Tuesday: 10 AM - 10:30 AM, 12 PM - 6 PM
                    Wednesday: Closed
                    Thursday: 10:30 AM - 6 PM
                    Friday: 10 AM - 1 AM
                    Saturday: 10 AM - 1 AM
                    Sunday: 12 PM - 9 PM
                    """.trimIndent(),
                bodyAsText(),
            )
        }
    }
}
