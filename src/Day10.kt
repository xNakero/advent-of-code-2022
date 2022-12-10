object Day10 {

    fun part1(): Int =
        with(readInput()) {
            generateSequence(20) { it + 40 }.take(6).toList()
                .map { (1 + this.take(it - 1).sum()) * it }
        }.sum()

    fun part2() {
        val registryByTime = readInput().let { commands ->
            commands.indices.map { command ->
                commands.take(command).sumOf { it } + 1
            }
        }
        List(registryByTime.size) { index ->
            if (registryByTime[index].let { it - 1..it + 1 }.any { it == (index % 40) }) {
                "#"
            } else {
                "."
            }
        }.chunked(40).forEach { println(it.joinToString("")) }
    }

    private fun readInput(): List<Int> = readInput("day10")
        .flatMap { line ->
            line.split(" ")
                .let { if (it[0] == "addx") listOf(0, it[1].toInt()) else listOf(0) }
        }
}

fun main() {
    println(Day10.part1())
    Day10.part2()
}