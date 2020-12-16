package dk.lessor

fun main() {
    val input = "0,1,5,10,3,12,19".split(",").map { it.toInt() }
    println(playGame(input).elementAt(2020 - input.size).first)
    println(playGame(input).elementAt(30000000 - input.size).first)
}


private fun playGame(input: List<Int>): Sequence<Pair<Int, Int>> {
    val map = input.dropLast(1).mapIndexed { i, v -> v to i }.toMap().toMutableMap()
    return generateSequence(input.last() to input.lastIndex) { current ->
        val next = map[current.first]?.let { current.second - it } ?: 0
        (next to (current.second + 1)).also { map[current.first] = current.second }
    }
}