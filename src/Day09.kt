import kotlin.math.pow
import kotlin.math.sign
import kotlin.math.sqrt

object Day09 {

    fun part1() = process(1)

    fun part2() = process(9)

    private fun process(tailSize: Int) = readCommands()
        .fold(Rope(tailSize)) { rope, cmd -> processCommand(cmd, rope) }
        .visitedCoordinates.size

    private fun processCommand(command: RopeCommand, rope: Rope): Rope =
        (0 until command.times).toList().fold(rope) { r, _ ->
            val updatedHead = r.head.move(command.direction)
            val updatedTail = updateTail(updatedHead, r)
            r.updated(updatedHead, updatedTail)
        }

    private fun updateTail(newHead: Coordinate, rope: Rope): Tail =
        (listOf(newHead) + rope.tail).toMutableList()
            .also { knots ->
                knots.indices.windowed(2, 1).forEach { pair ->
                    takeIf { knots[pair[0]].isRequiredToMove(knots[pair[1]]) }
                        ?.also { knots[pair[1]] = knots[pair[1]].move(knots[pair[0]]) }
                }
            }.let { it.subList(1, it.size).toList() }

    private fun readCommands() = readInput("day09")
        .map { line -> line.split(" ").let { RopeCommand(it[0], it[1].toInt()) } }
}


data class Rope(val head: Coordinate, val tail: Tail, val visitedCoordinates: Set<Coordinate>) {

    constructor(tailSize: Int) : this(
        head = Coordinate(),
        tail = (0 until tailSize).toList().map { Coordinate() },
        visitedCoordinates = emptySet()
    )

    fun updated(updatedHead: Coordinate, updatedTail: Tail): Rope =
        copy(
            head = updatedHead,
            tail = updatedTail,
            visitedCoordinates = visitedCoordinates + updatedTail.last()
        )
}

data class Coordinate(val x: Int = 0, val y: Int = 0) {

    fun move(direction: String): Coordinate =
        when (direction) {
            "L" -> copy(x = x - 1)
            "R" -> copy(x = x + 1)
            "U" -> copy(y = y + 1)
            "D" -> copy(y = y - 1)
            else -> throw InvalidDirectionException(direction)
        }

    fun move(other: Coordinate): Coordinate =
        Coordinate(
            x = (other.x - x).sign + x,
            y = (other.y - y).sign + y
        )

    fun isRequiredToMove(other: Coordinate): Boolean = distanceToOther(other) !in setOf(0.0, 1.0, sqrt(2.0))

    private fun distanceToOther(other: Coordinate): Double =
        sqrt((x - other.x.toDouble()).pow(2) + (y - other.y.toDouble()).pow(2))
}

data class RopeCommand(val direction: String, val times: Int)

private typealias Tail = List<Coordinate>

class InvalidDirectionException(direction: String) : RuntimeException("There is no such direction: $direction")

fun main() {
    println(Day09.part1())
    println(Day09.part2())
}