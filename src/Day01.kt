class Day01 {

    fun part1(): Int = readInput().maxOf { it.sum() }

    fun part2(): Int = readInput().map { it.sum() }.sortedDescending().take(3).sum()

    private fun readInput(): List<List<Int>> = readInput("day01").joinToString()
            .split(", , ")
            .map { elf ->
                elf.split(", ").map { it.toInt() }
            }
}

fun main() {
    val day1 = Day01()
    println(day1.part1())
    println(day1.part2())
}