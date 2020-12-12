package dk.lessor

import dk.lessor.Instruction.Jmp
import dk.lessor.Instruction.Nop

fun main() {
    val lines = readFile("day_8.txt")
    val instructions = lines.map(Instruction.Companion::parser).toMutableList()
    println(findLoop(instructions))
    println(part2(instructions))
}

fun findLoop(instructions: List<Instruction>): Int {
    var acc = 0
    var pos = 0
    val visited = mutableListOf<Int>()

    while (true) {
        visited += pos
        val next = when (val instruction = instructions[pos]) {
            is Jmp -> pos + instruction.amount
            is Instruction.Acc -> {
                acc += instruction.value
                pos + 1
            }
            is Nop -> pos + 1
        }

        if (next >= instructions.size || visited.contains(next)) break

        pos = next
    }

    return acc
}

fun part2(instructions: List<Instruction>): Int {
    var acc = 0
    var pos = 0
    var flip = 0
    val visited = mutableListOf<Int>()

    while (true) {
        visited += pos

        val instruction = if (flip == pos) {
            when (val i = instructions[pos]) {
                is Jmp -> Nop(i.amount)
                is Nop -> Jmp(i.amount)
                is Instruction.Acc -> i
            }
        } else instructions[pos]

        val next = when (instruction) {
            is Jmp -> pos + instruction.amount
            is Instruction.Acc -> {
                acc += instruction.value
                pos + 1
            }
            is Nop -> pos + 1
        }

        if (visited.contains(next)) {
            flip += 1
            acc = 0
            pos = 0
            visited.clear()
            continue
        }

        if (flip >= instructions.lastIndex || next >= instructions.size) break

        pos = next
    }

    return acc
}

sealed class Instruction {
    data class Nop(val amount: Int) : Instruction()
    data class Jmp(val amount: Int) : Instruction()
    data class Acc(val value: Int) : Instruction()

    companion object {
        fun parser(line: String): Instruction {
            val i = line.drop(4).toInt()
            return when (line.take(3)) {
                "jmp" -> Jmp(i)
                "acc" -> Acc(i)
                else -> Nop(i)
            }
        }
    }
}