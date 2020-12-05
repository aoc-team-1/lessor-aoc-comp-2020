package dk.lessor

fun main() {
    val lines = readFile("day_5.txt")
    val b = lines.map(BoardingPass.Companion::parse).sortedBy { it.id }
    println(b.last().id)
    println(findId(b))
}

fun findId(boardingPasses: List<BoardingPass>): Int {
    for ((index, bp) in boardingPasses.withIndex()) {
        if (index == 0) continue

        val last = boardingPasses[index - 1]
        if (last.id + 1 != bp.id) return last.id + 1
    }

    return 0
}

data class BoardingPass(
    val row: Int,
    val column: Int,
) {
    val id: Int
        get() = row * 8 + column

    companion object {
        fun parse(input: String): BoardingPass {
            val row = traverseRows(input.take(7))
            val column = traverseColumn(input.drop(7))
            return BoardingPass(row, column)
        }

        private fun traverseRows(rows: String): Int {
            return traverse(rows, 'F', (0..127))
        }

        private fun traverseColumn(columns: String): Int {
            return traverse(columns, 'L', (0..7))
        }

        private fun traverse(path: String, check: Char, range: IntRange): Int {
            if (path.isEmpty()) {
                return range.first
            }

            val split = (range.last - range.first) / 2

            val newRange = if (path.first() == check) {
                (range.first..range.first + split)
            } else {
                (range.first + split + 1..range.last)
            }

            return traverse(path.drop(1), check, newRange)
        }

    }
}