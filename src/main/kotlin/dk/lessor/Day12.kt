package dk.lessor

import kotlin.math.absoluteValue

fun main() {
    val directions = readFile("day_12.txt").map { parseDirectionInput(it) }
    val option1 = navigateStorm(directions, Ship::move)
    println(option1.distance)
    val option2 = navigateStorm(directions, Ship::moveWithWaypoint)
    println(option2.distance)
}

fun navigateStorm(directions: List<Direction>, move: (Ship, Direction) -> Unit): Ship {
    val ship = Ship()
    directions.forEach { move(ship, it) }
    return ship
}

fun parseDirectionInput(line: String): Direction {
    val amount = line.drop(1).toInt()

    return when (line.first()) {
        'N' -> Direction.North(amount)
        'E' -> Direction.East(amount)
        'S' -> Direction.South(amount)
        'W' -> Direction.West(amount)
        'R' -> Direction.Right(amount)
        'L' -> Direction.Left(amount)
        'F' -> Direction.Forwards(amount)
        else -> Direction.North(0)
    }
}

class Ship {
    private var x = 0
    private var y = 0
    private var direction = 1 to 0
    private var waypoint = 10 to 1

    val distance
        get() = x.absoluteValue + y.absoluteValue

    fun move(d: Direction) {
        when (d) {
            is Direction.North -> y += d.amount
            is Direction.East -> x += d.amount
            is Direction.South -> y -= d.amount
            is Direction.West -> x -= d.amount
            is Direction.Forwards -> {
                x += (direction.first * d.amount)
                y += (direction.second * d.amount)
            }
            is Direction.Left -> direction = turnPoint(d.amount, direction)
            is Direction.Right -> direction = turnPoint(-d.amount, direction)
        }
    }

    fun moveWithWaypoint(d: Direction) {
        when (d) {
            is Direction.North -> waypoint = waypoint.copy(second = waypoint.second + d.amount)
            is Direction.East -> waypoint = waypoint.copy(first = waypoint.first + d.amount)
            is Direction.South -> waypoint = waypoint.copy(second = waypoint.second - d.amount)
            is Direction.West -> waypoint = waypoint.copy(first = waypoint.first - d.amount)
            is Direction.Forwards -> {
                x += (waypoint.first * d.amount)
                y += (waypoint.second * d.amount)
            }
            is Direction.Left -> waypoint = turnPoint(d.amount, waypoint)
            is Direction.Right -> waypoint = turnPoint(-d.amount, waypoint)
        }

    }

    private fun turnPoint(degrees: Int, point: Pair<Int, Int>): Pair<Int, Int> {
        return if (degrees == 90 || degrees == -270) point.copy(-point.second, point.first)
        else if (degrees == 180 || degrees == -180) point.copy(-point.first, -point.second)
        else point.copy(point.second, -point.first)
    }
}

sealed class Direction {
    data class North(val amount: Int) : Direction()
    data class East(val amount: Int) : Direction()
    data class South(val amount: Int) : Direction()
    data class West(val amount: Int) : Direction()
    data class Left(val amount: Int) : Direction()
    data class Right(val amount: Int) : Direction()
    data class Forwards(val amount: Int) : Direction()
}