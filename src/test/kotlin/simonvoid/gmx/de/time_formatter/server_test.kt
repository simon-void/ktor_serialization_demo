package simonvoid.gmx.de.time_formatter

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ServerTest {
    @Test
    fun `test toStatusEvents`() {
        val actualEvents: List<StatusEvent> = mapOf(
            "monday" to emptyList(),
            "tuesday" to listOf(
                IncomingOpeningTime(type = "open", value = UnixTime(36000)),
                IncomingOpeningTime(type = "close", value = UnixTime(37800)),
            )
        ).toStatusEvents()

        assertEquals(
            listOf(
                ClosedEvent(Weekday.MONDAY),
                OpeningEvent(Weekday.TUESDAY, time = UnixTime(36000)),
                ClosingEvent(Weekday.TUESDAY, time = UnixTime(37800)),
            ),
            actualEvents
        )
    }

    @Test
    fun `test toOpenPeriodsByDay`() {
        val actualOpenPeriodsByDay: List<Pair<Weekday, List<OpenPeriod>>> = listOf(
            ClosedEvent(Weekday.MONDAY),
            OpeningEvent(Weekday.TUESDAY, time = UnixTime(36000)),
            ClosingEvent(Weekday.TUESDAY, time = UnixTime(37800)),
            OpeningEvent(Weekday.TUESDAY, time = UnixTime(75600)),
            ClosingEvent(Weekday.WEDNESDAY, time = UnixTime(3600)),
        ).toOpenPeriodsByDay()

        assertEquals(
            listOf(
                Weekday.MONDAY to emptyList(),
                Weekday.TUESDAY to listOf(
                    OpenPeriod(from = UnixTime(36000), to=UnixTime(37800)),
                    OpenPeriod(from = UnixTime(75600), to=UnixTime(3600)),
                )
            ),
            actualOpenPeriodsByDay
        )
    }
}