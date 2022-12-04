class Day04 {

    fun part1() = parseInput().count { it[0].containsAll(it[1]) || it[1].containsAll(it[0]) }

    fun part2() = parseInput().count { it[0].intersect(it[1].toSet()).isNotEmpty() }

    private fun parseInput() =
        readInput("day04")
            .map { line -> line.split(",").map { it.split("-") } }
            .map { pair -> pair.map { (it[0].toInt()..it[1].toInt()).toList() } }
}

fun main() {
    val day4 = Day04()
    println(day4.part1())
    println(day4.part2())
}