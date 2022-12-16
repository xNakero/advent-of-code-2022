import Status.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

object Day13 {

    fun part1(): Int =
        parse()
            .mapIndexed { index, (first, second) -> (index + 1) to compareElements(first, second) }
            .filter { it.second == LESSER }
            .sumOf { it.first }

    fun part2(): Int =
        parse()
            .map { listOf(it.first, it.second) }
            .flatten()
            .let { it + "[[2]]".toPacket() + "[[6]]".toPacket() }
            .sortedWith { first, second -> compareElements(first as JsonElement, second as JsonElement).representation }
            .let { (it.indexOf("[2]".toPacket()) + 1) * (it.indexOf("[6]".toPacket()) + 1) }

    private fun compareElements(first: JsonElement, second: JsonElement): Status =
        when {
            first is JsonArray && second is JsonArray -> {
                compareArrays(first, second)
            }

            first is JsonPrimitive && second is JsonPrimitive -> {
                compareValues(first, second)
            }

            first is JsonPrimitive && second is JsonArray -> {
                compareElements(JsonArray(listOf(first)), second)
            }

            first is JsonArray && second is JsonPrimitive -> {
                compareElements(first, JsonArray(listOf(second)))
            }

            else -> throw IllegalStateException("No such set of elements is allowed")
        }

    private fun compareValues(first: JsonPrimitive, second: JsonPrimitive): Status =
        calculateOne(first.toString().toInt(), second.toString().toInt())

    private fun compareArrays(first: JsonArray, second: JsonArray): Status {
        repeat((0 until minOf(first.size, second.size)).count()) { index ->
            val result = compareElements(first[index], second[index])
            if (result != EQUAL) {
                return result
            }
        }
        return calculateOne(first.size, second.size)
    }

    private fun calculateOne(first: Int, second: Int): Status {
        val result = first - second
        return when {
            result < 0 -> LESSER
            result == 0 -> EQUAL
            else -> BIGGER
        }
    }

    private fun parse(): List<Pair<JsonArray, JsonArray>> = readInput("day13")
        .windowed(2, 3)
        .map { (first, second) -> Pair(first.toPacket(), second.toPacket()) }
}

private fun String.toPacket(): JsonArray = Json.decodeFromString(this)

enum class Status(val representation: Int) {
    LESSER(-1), BIGGER(1), EQUAL(0)
}

fun main() {
    println(Day13.part1())
    println(Day13.part2())
}