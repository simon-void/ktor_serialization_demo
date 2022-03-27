package simonvoid.gmx.de.time_formatter

import java.util.*

fun String.toWeekday(): Weekday = when(this.lowercase()) {
    "monday" -> Weekday.MONDAY
    "tuesday" -> Weekday.TUESDAY
    "wednesday" -> Weekday.WEDNESDAY
    "thursday" -> Weekday.THURSDAY
    "friday" -> Weekday.FRIDAY
    "saturday" -> Weekday.SATURDAY
    "sunday" -> Weekday.SUNDAY
    else -> throw IllegalArgumentException("unknown weekday: $this")
}

fun List<Pair<Weekday, List<OpenPeriod>>>.prettyPrint(): String {
    val sb = StringBuilder()
    this.forEach { (day, periods) ->
        sb.append(day.prettyPrint())
        sb.append(": ")
        if(periods.isEmpty()) {
            sb.append("Closed")
        } else {
            sb.append(periods.joinToString(separator = ", "))
        }
        sb.append('\n')
    }
    if(this.isNotEmpty()) {
        // delete the last '\n'
        sb.deleteCharAt(sb.length-1)
    }

    return sb.toString()
}

private fun Weekday.prettyPrint(): String = this.name.lowercase()
    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }