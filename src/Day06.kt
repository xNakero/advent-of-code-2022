object Day06 {

    fun part1(): Int = process(4)

    fun part2(): Int = process(14)

    private fun process(length: Int): Int =
        readInputAsString("day06")
            .let { input ->
                (length..input.length).first { input.substring(it - length, it).toSet().size == length }
            }
}

fun main() {
    println(Day06.part1())
    println(Day06.part2())
}