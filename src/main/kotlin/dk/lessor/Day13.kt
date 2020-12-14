package dk.lessor

fun main() {
    val lines = readFile("day_13.txt")
    val timestamp = lines.first().toLong()
    val busses = lines.last().split(",").toList()
    println(simpleBusTable(timestamp, busses))
    println(busContestSolver(busses))
}

fun simpleBusTable(goal: Long, busses: List<String>): Long {
    val table = busses.filter { it != "x" }.associate { it.toLong() to it.toLong() }.toMutableMap()

    while (true) {
        for (bus in table) {
            val last = bus.value
            val next = last + bus.key
            table[bus.key] = next
        }

        if (table.any { it.value >= goal }) break
    }

    val earliest = table
        .filterValues { it >= goal }
        .toList()
        .sortedBy { (_, value) -> value }
        .first()


    return earliest.first * (earliest.second - goal)
}

fun busContestSolver(busses: List<String>): Long {
    val ids = busses.mapIndexedNotNull { index, value -> if (value == "x") null else index to value.toLong() }
    var current = 0L
    var timeJump = 1L
    for ((start, step) in ids) {
        while ((current + start) % step != 0L) current += timeJump
        timeJump *= step
    }

    return current
}