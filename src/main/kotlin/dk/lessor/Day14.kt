package dk.lessor

typealias Manipulator = (Long, Long, String, MutableMap<Long, Long>) -> Unit

fun main() {
    val lines = readFile("day_14.txt")
    println(decodeBitMask(lines, valueDecoder))
    println(decodeBitMask(lines, memoryDecoder))
}

val valueDecoder: Manipulator = { pos, value, mask, memory -> memory[pos] = value.applyMask(mask) }
val memoryDecoder: Manipulator = { pos, value, mask, memory -> pos.floatingMask(mask).forEach { memory[it] = value } }

fun decodeBitMask(lines: List<String>, manipulator: Manipulator): Long {
    var mask = ""
    val memory = mutableMapOf<Long, Long>()
    val memPattern = "mem\\[(\\d+)]".toRegex()

    for (line in lines) {
        if (line.startsWith("mask")) {
            mask = line.split(" = ").last()
            continue
        }

        val (mem, value) = line.split(" = ")
        val pos = memPattern.find(mem)!!.groupValues[1].toLong()

        manipulator(pos, value.toLong(), mask, memory)
    }

    return memory.values.sum()
}

fun Long.applyMask(mask: String): Long {
    val original = toString(2).padStart(mask.length, '0')

    val masked = mask.mapIndexed { i, bit -> if (bit == 'X') original[i] else bit }.joinToString("")
    return masked.toLong(2)
}

fun Long.floatingMask(mask: String): List<Long> {
    val original = toString(2).padStart(mask.length, '0')
    val result = mutableListOf(mutableListOf<Char>())

    for (i in 0..mask.lastIndex) {
        when (mask[i]) {
            '0' -> result.forEach { it.add(original[i]) }
            '1' -> result.forEach { it.add('1') }
            else -> {
                val copy = result.map { it.toMutableList() }
                copy.forEach { it.add('1') }
                result.forEach { it.add('0') }
                result.addAll(copy)
            }
        }
    }

    return result.map { it.joinToString("").toLong(2) }
}