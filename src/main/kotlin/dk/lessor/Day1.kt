package dk.lessor

fun main() {
    val input = readFile("day_1.txt").map { it.toInt() }
    println(findSum(input))
    println(findSecondSum(input.map { it.toLong() }))
}

fun findSum(input: List<Int>): Int {
    for (i in 0 until input.lastIndex) {
        for (j in i + 1..input.lastIndex) {
            if (input[i] + input[j] == 2020) return input[i] * input[j]
        }
    }

    return 0
}

fun findSecondSum(input: List<Long>): Long {
    for (i in 0 until input.lastIndex - 1) {
        for (j in i + 1 until input.lastIndex) {
            for (k in j + 1..input.lastIndex) {
                if (input[i] + input[j] + input[k] == 2020L) return input[i] * input[j] * input[k]
            }
        }
    }
    return 0L
}