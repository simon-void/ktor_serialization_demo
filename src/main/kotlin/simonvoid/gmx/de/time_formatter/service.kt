package simonvoid.gmx.de.time_formatter

fun Map<String, List<IncomingOpeningTime>>.toStatusEvents(): List<StatusEvent> =
    this.flatMap { (weekdayStr: String, openingTimes: List<IncomingOpeningTime>) ->
        val weekday = weekdayStr.toWeekday()
        if(openingTimes.isEmpty()) {
            listOf(ClosedEvent(weekday))
        } else {
            openingTimes.map { incomingTime ->
                when(val type = incomingTime.type.lowercase()) {
                    "open" -> OpeningEvent(weekday, incomingTime.value)
                    "close" -> ClosingEvent(weekday, incomingTime.value)
                    else -> throw IllegalArgumentException("unknown type: $type")
                }
            }
        }
    }

fun List<StatusEvent>.toOpenPeriodsByDay(): List<Pair<Weekday, List<OpenPeriod>>> {
    val intermediateResult = mutableListOf<Pair<Weekday, List<Pair<OpeningEvent, ClosingEvent>>>>()

    // i need a data structure that i can peek into
    val eventDeque = ArrayDeque(this)
    while (eventDeque.isNotEmpty()) {
        intermediateResult.add(eventDeque.getNextDay())
    }

    // make sure each day is treated at most once
    run{
        val daysThatOccurMoreThanOnce = intermediateResult
            .map { (day, _)->day }
            .groupBy { it }
            .mapNotNull { (day, listOfThisDay)->
                if(listOfThisDay.size==1) {
                    null
                } else {
                    day
                }
            }
        if(daysThatOccurMoreThanOnce.isNotEmpty()) {
            throw IllegalArgumentException(
                "each day may only occur once in list, but the following days occur multiple times: $daysThatOccurMoreThanOnce"
            )
        }
    }

    return intermediateResult.map { (day, openingWithClosingEvents)->
        day to openingWithClosingEvents.map { (openingEvent, closingEvent)->
            val openingTime = openingEvent.time
            val closingTime = closingEvent.time

            // some more checks
            when {
                closingEvent.day==day -> {
                    if(closingTime<=openingTime) {
                        throw IllegalArgumentException("closing time $closingTime before opening time $openingTime on ${day.name}")
                    }
                }
                closingEvent.day.isFollowingDayOf(day) -> {
                    if(closingTime>=openingTime) {
                        throw IllegalArgumentException("closing time $closingTime more than 24h after opening time $openingTime on $day")
                    }
                }
                else -> throw IllegalArgumentException(
                    "closing day (${closingEvent.day.name}) has to be same or following day of opening day (${day.name}) but isn't"
                )
            }
            OpenPeriod(openingEvent.time, closingEvent.time)
        }
    }
}

private fun ArrayDeque<StatusEvent>.getNextDay(): Pair<Weekday, List<Pair<OpeningEvent, ClosingEvent>>> {
    val nextEvent = this.first()
    val day = nextEvent.day

    return day to if (nextEvent is ClosedEvent) {
        this.removeFirst()
        emptyList()
    } else {
        this.getMatchingTimesFor(day)
    }
}

private fun ArrayDeque<StatusEvent>.getMatchingTimesFor(day: Weekday): List<Pair<OpeningEvent, ClosingEvent>> {
    val matchingEntries = mutableListOf<Pair<OpeningEvent, ClosingEvent>>()
    while(this.firstOrNull()?.day==day) {
        val firstEvent = this.removeFirst()
        if (firstEvent !is OpeningEvent) {
            throw IllegalArgumentException(
                "first event for day ${day.name.lowercase()} has to be of type OpeningEvent but was ${firstEvent::class.simpleName}"
            )
        }
        val secondEvent = this.removeFirstOrNull() ?: throw IllegalArgumentException(
            "no ClosingEvent for last OpeningEvent"
        )
        if (secondEvent !is ClosingEvent) {
            throw IllegalArgumentException(
                "event following openingEvent on day ${day.name.lowercase()} has to be of type ClosingEvent but was ${secondEvent::class.simpleName}"
            )
        }
        matchingEntries.add(firstEvent to secondEvent)
    }
    return matchingEntries
}