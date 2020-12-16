package dk.lessor

fun main() {
    val lines = readFile("day_16.txt")
    val (ranges, myTicket, otherTickets) = parseTicketInformation(lines)

    println(otherInvalidTickets(ranges, otherTickets))
    println(determineTicketFields(ranges, myTicket, otherTickets))
}

fun parseTicketInformation(lines: List<String>): Triple<Map<String, List<IntRange>>, List<Int>, List<List<Int>>> {
    val ranges = lines.take(20).associate {
        val key = it.split(": ").first()
        val r = it.split(": ").last().split(" or ").map { range ->
            val (start, end) = range.split("-").map(String::toInt)
            IntRange(start, end)
        }

        key to r
    }

    val ticket = lines.drop(22).take(1).first().split(",").map(String::toInt).toList()

    val otherTicket = lines.drop(25).map { it.split(",").map(String::toInt) }

    return Triple(ranges, ticket, otherTicket)
}

fun otherInvalidTickets(ranges: Map<String, List<IntRange>>, otherTickets: List<List<Int>>): Int {
    val r = ranges.values.flatten()
    val t = otherTickets.flatten()

    return t.filter { v -> !r.any { it.contains(v) } }.sum()
}

fun determineTicketFields(ranges: Map<String, List<IntRange>>, myTicket: List<Int>, otherTickets: List<List<Int>>): Long {
    val r = ranges.values.flatten()
    val t = otherTickets.filter { it.validateTicket(r) }
    val fieldValues = Array<MutableList<Int>>(myTicket.size) { mutableListOf() }

    for (ticket in t) {
        for (i in 0..myTicket.lastIndex) {
            fieldValues[i].add(ticket[i])
        }
    }

    val possibleRanges = Array<MutableList<String>>(myTicket.size) { mutableListOf() }
    for (i in 0..fieldValues.lastIndex) {
        for (range in ranges) {
            if (fieldValues[i].validateTicket(range.value)) possibleRanges[i].add(range.key)
        }
    }

    while (possibleRanges.any { it.size != 1 }) {
        val unique = possibleRanges.filter { it.size == 1 }.flatten()
        possibleRanges.filter { it.size > 1 && it.any { name -> unique.contains(name) } }.forEach { it.removeAll(unique) }
    }

    var sum = 1L
    for (i in 0..possibleRanges.lastIndex) {
        if (possibleRanges[i].first().startsWith("departure")) sum *= myTicket[i]
    }

    return sum
}

fun List<Int>.validateTicket(ranges: List<IntRange>): Boolean {
    return all { v -> ranges.any { it.contains(v) } }
}