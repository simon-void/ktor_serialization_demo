package simonvoid.gmx.de.time_formatter

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class UnixTime(private val value: Int): Comparable<UnixTime> {
    init {
        require(value in (0..86399)) {
            "value has to lie within 0 (0:00:00 AM) to 86399 (11:59:59 PM) but was $value"
        }
    }
    override fun toString(): String = toDisplay()

    override fun compareTo(other: UnixTime) = this.value.compareTo(other.value)

    private fun toDisplay(): String {
        val seconds = value % 60
        val minutesInDay = value / 60
        val minutes = minutesInDay % 60
        val hoursInDay = minutesInDay / 60

        return buildString {
            if(hoursInDay>12) {
                append(hoursInDay-12)
            } else {
                append(hoursInDay)
            }
            if(minutes!=0 || seconds!=0) {
                append(":$minutes")
                if(seconds!=0) {
                    append(":$seconds")
                }
            }
            if (hoursInDay >= 12) {
                append(" PM")
            } else {
                append(" AM")
            }
        }
    }
}

@Serializable
data class IncomingOpeningTime(
    val type: String,
    val value: UnixTime,
)

enum class Weekday {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;

    fun isFollowingDayOf(other: Weekday): Boolean = this.ordinal == (other.ordinal+1) % entries.size
}

data class OpenPeriod(
    val from: UnixTime,
    val to: UnixTime,
) {
    override fun toString() = "$from - $to"
}

sealed interface StatusEvent {
    val day: Weekday
}

data class OpeningEvent(
    override val day: Weekday,
    val time: UnixTime,
): StatusEvent

data class ClosingEvent(
    override val day: Weekday,
    val time: UnixTime,
): StatusEvent

data class ClosedEvent(
    override val day: Weekday,
): StatusEvent
