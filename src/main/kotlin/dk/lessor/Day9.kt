package dk.lessor

fun main() {
    val xmas = readFile("day_9.txt").map { it.toLong() }
    val invalid = analyzeXmas(xmas)
    println(invalid)
    println(findEncryptionWeakness(xmas, invalid))
}

fun analyzeXmas(xmas: List<Long>, preambleSize: Int = 25): Long {
    for (i in preambleSize..xmas.lastIndex) {
        val number = xmas[i]
        val preamble = calculatePreambleValues(xmas.drop(i - preambleSize).take(preambleSize))
        if (!preamble.contains(number)) return number
    }

    return 0
}

fun calculatePreambleValues(preamble: List<Long>): Set<Long> {
    val result = mutableSetOf<Long>()
    for (i in 0 until preamble.lastIndex) {
        for (j in i + 1..preamble.lastIndex) {
            result += preamble[i] + preamble[j]
        }
    }

    return result
}

fun findEncryptionWeakness(xmas: List<Long>, invalid: Long): Long {
    var longest = listOf<Long>()
    for (i in 0..xmas.lastIndex) {
        val temp = findLongestCain(xmas.drop(i), invalid)
        if (temp.size > longest.size) longest = temp
    }

    longest = longest.sorted()
    return longest.first() + longest.last()
}

fun findLongestCain(xmas: List<Long>, invalid: Long): List<Long> {
    val result = mutableListOf<Long>()
    var sum = 0L
    for (number in xmas) {
        result.add(number)
        sum += number
        if (sum > invalid) return emptyList()
        if (sum == invalid) return result
    }

    return emptyList()
}
