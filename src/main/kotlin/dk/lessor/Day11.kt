package dk.lessor

val chairs: (Char) -> Int = { if (it == '#') 1 else 0 }

fun main() {
    val map = readFile("day_11.txt").map { it.toList() }
    println(gameOfChairs(map))
    println(gameOfVisibleChairs(map))
}

fun gameOfChairs(chairMap: List<List<Char>>): Int {
    var oldMap = chairMap.map { it.toMutableList() }.toMutableList()

    while (true) {
        val newMap = oldMap.map { it.toMutableList() }.toMutableList()

        for (y in 0..oldMap.lastIndex) {
            val row = oldMap[y]
            for (x in 0..row.lastIndex) {
                val type = row[x]
                if (type == '.') continue
                if (type == 'L' && !neighbours(oldMap, x, y).any { it == '#' }) newMap[y][x] = '#'
                else if (type == '#' && neighbours(oldMap, x, y).sumBy { chairs(it) } >= 4) newMap[y][x] = 'L'
            }
        }

        if (newMap == oldMap) break
        oldMap = newMap
    }


    return oldMap.flatten().sumBy { chairs(it) }
}

fun gameOfVisibleChairs(chairMap: List<List<Char>>): Int {
    var oldMap = chairMap.map { it.toMutableList() }.toMutableList()

    while (true) {
        val newMap = oldMap.map { it.toMutableList() }.toMutableList()

        for (y in 0..oldMap.lastIndex) {
            val row = oldMap[y]
            for (x in 0..row.lastIndex) {
                val type = row[x]
                if (type == '.') continue
                if (type == 'L' && !visibleChairs(oldMap, x, y).any { it == '#' }) newMap[y][x] = '#'
                else if (type == '#' && visibleChairs(oldMap, x, y).sumBy { chairs(it) } >= 5) newMap[y][x] = 'L'
            }
        }

        if (newMap == oldMap) break
        oldMap = newMap
    }


    return oldMap.flatten().sumBy { chairs(it) }
}

fun neighbours(chairMap: List<List<Char>>, x: Int, y: Int): List<Char> {
    val n = chairMap.getOrNull(x, y - 1)
    val ne = chairMap.getOrNull(x + 1, y - 1)
    val e = chairMap.getOrNull(x + 1, y)
    val se = chairMap.getOrNull(x + 1, y + 1)
    val s = chairMap.getOrNull(x, y + 1)
    val sw = chairMap.getOrNull(x - 1, y + 1)
    val w = chairMap.getOrNull(x - 1, y)
    val nw = chairMap.getOrNull(x - 1, y - 1)

    return listOfNotNull(n, ne, e, se, s, sw, w, nw)
}

fun visibleChairs(chairMap: List<List<Char>>, x: Int, y: Int): List<Char> {
    val n = chairMap.walk(x to y, walkColumn = { it - 1 })
    val ne = chairMap.walk(x to y, { it + 1 }, { it - 1 })
    val e = chairMap.walk(x to y, walkRow = { it + 1 })
    val se = chairMap.walk(x to y, { it + 1 }, { it + 1 })
    val s = chairMap.walk(x to y, walkColumn = { it + 1 })
    val sw = chairMap.walk(x to y, { it - 1 }, { it + 1 })
    val w = chairMap.walk(x to y, walkRow = { it - 1 })
    val nw = chairMap.walk(x to y, { it - 1 }, { it - 1 })

    return listOfNotNull(n, ne, e, se, s, sw, w, nw)
}

fun List<List<Char>>.walk(initial: Pair<Int, Int>, walkRow: (Int) -> Int = { it }, walkColumn: (Int) -> Int = { it }): Char? {
    var (x, y) = initial
    var current: Char? = '.'

    while (current != null && current == '.') {
        x = walkRow(x)
        y = walkColumn(y)
        current = getOrNull(x, y)
    }

    return current
}

fun List<List<Char>>.getOrNull(x: Int, y: Int): Char? {
    return getOrNull(y)?.getOrNull(x)
}