package simonvoid.gmx.de.time_formatter

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.assertEquals
import kotlin.test.fail

class UnixTimeTest {
    @ParameterizedTest
    @ValueSource(ints = [-1, 86400])
    fun `test constructor checks for out of bounds values`(time: Int) {
        try {
            UnixTime(time)
            fail("should have failed because it leaves the [0:00:00-23:59:59] bounds")
        } catch (_: IllegalArgumentException){}
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 86399])
    fun `test constructor's outer bounds`(time: Int) {
        UnixTime(time)
    }

    @ParameterizedTest
    @CsvSource(value = ["0,0 AM", "36000,10 AM", "37800,10:30 AM", "43200,12 PM", "75600,9 PM", "86399,11:59:59 PM"])
    fun `test toString()`(timeValue: String, expectedFormat: String) {
        assertEquals(
            expectedFormat,
            UnixTime(timeValue.toInt()).toString()
        )
    }
}
