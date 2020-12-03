package dk.lessor

fun main() {
    val lines = readFile("day_3.txt")
    val trees = testSlope(lines)
    println(trees)

    val result = tobogganNavigator(lines)
    println(result)
}

fun testSlope(lines: List<String>, incline: Int = 3, step: Int = 1): Int {
    var pos = 0
    var trees = 0

    for (i in 0..lines.lastIndex step step) {
        val line = lines[i]
        trees += if (line[pos] == '#') 1 else 0
        pos = (pos + incline) % line.length
    }

    return trees
}

fun tobogganNavigator(lines: List<String>): Int {
    val slops = listOf(1 to 1, 3 to 1, 5 to 1, 7 to 1, 1 to 2)

    var result = 1
    for (slop in slops) {
        result *= testSlope(lines, slop.first, slop.second)
    }

    return result
}