class Day05 {

    private val input = readInput("day05")
    private val commands = readCommands()

    fun part1(): String = readCrates()
        .let { commands.fold(it) { c, cmd -> moveCratesByOne(cmd, c) } }
        .toOutput()

    fun part2(): String = readCrates()
        .let { commands.fold(it) { c, cmd -> moveCratesBatch(cmd, c) } }
        .toOutput()

    private fun moveCratesByOne(cmd: Command, crates: List<ArrayDeque<String>>): List<ArrayDeque<String>> {
        repeat((0 until cmd.count).count()) {
            crates[cmd.destination].add(crates[cmd.source].removeLast())
        }
        return crates
    }

    private fun moveCratesBatch(cmd: Command, crates: List<ArrayDeque<String>>): List<ArrayDeque<String>> {
        crates[cmd.destination].addAll(crates[cmd.source].takeLast(cmd.count))
        repeat((0 until cmd.count).count()) {
            crates[cmd.source].removeLast()
        }
        return crates
    }

    private fun readCrates() = input
        .subList(0, input.indexOfFirst { it.isBlank() })
        .map { it.chunked(4).map { it1 -> it1.replace(" ", "") } }
        .let { i ->
            val crates = i.dropLast(1)
            List(i.last().size) { index -> crates.map { if (it.size > index) it[index] else "" } }
        }
        .map { stack -> stack.filter { it.isNotBlank() }.reversed() }
        .toArrayDeque()

    private fun readCommands() = input
        .subList(input.indexOfFirst { it.isBlank() } + 1, input.lastIndex + 1)
        .map { line ->
            line.filter { it.isDigit() || it == ' ' }
                .split(" ")
                .filter { it.isNotBlank() }
        }
        .map { Command(count = it[0].toInt(), source = it[1].toInt() - 1, destination = it[2].toInt() - 1) }
}

private fun List<List<String>>.toArrayDeque() = map { ArrayDeque<String>().apply { this.addAll(it) } }

private fun List<ArrayDeque<String>>.toOutput() = map { it.last()[1] }.joinToString("")

data class Command(val count: Int, val source: Int, val destination: Int)

fun main() {
    val day5 = Day05()
    println(day5.part1())
    println(day5.part2())
}