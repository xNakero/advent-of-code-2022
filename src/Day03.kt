class Day03 {

    fun part1() = readInput("day03")
        .map { it.chunked(it.length / 2) }
        .map { rucksack -> rucksack.map { it.chunked(1) } }
        .flatMap { it[0].intersect(it[1].toSet()) }
        .map { it.toCharArray()[0] }
        .sumOf { priority(it) }

    fun part2() = readInput("day03")
        .chunked(3)
        .flatMap {
            it[0].toCharArray()
                .intersect(it[1].toSet())
                .intersect(it[2].toSet())
        }
        .sumOf { priority(it) }

    private fun priority(character: Char) =
        when {
            character.isUpperCase() -> character.code - 38
            else -> character.code - 96
        }
}

fun main() {
    val day3 = Day03()
    println(day3.part1())
    println(day3.part2())
}