package dk.lessor

fun main() {
    val lines = readFile("day_6.txt").toMutableList()
    lines.add("")
    val distinctAnswers = anyAnswerParser(lines)
    println(distinctAnswers.map { it.sumBy { 1 } }.sum())
    println(allAnswerParser(lines).map { it.sumBy { 1 } }.sum())
}

fun anyAnswerParser(lines: List<String>): List<String> {
    val result = mutableListOf<String>()

    val current = StringBuilder()
    for (line in lines) {
        if (line.isNotBlank()) {
            current.append(line)
            continue
        }
        result += current.toList().distinct().joinToString("")

        current.clear()
    }

    return result
}

fun allAnswerParser(lines: List<String>): List<String> {
    val result = mutableListOf<String>()

    val current = mutableListOf<String>()
    for (line in lines) {
        if (line.isNotBlank()) {
            current.add(line)
            continue
        }

        current.sortBy { it.length }
        result += current.fold(current.first()) { currentAnswers, next -> currentAnswers.filter { next.contains(it) } }
        current.clear()
    }

    return result
}