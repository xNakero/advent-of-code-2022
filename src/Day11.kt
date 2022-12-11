object Day11 {

    fun part1() = solve(20) { level, _ -> level / 3}

    fun part2() = solve(10_000) {level, mod -> level % mod}

    private fun solve(rounds: Int, worryLevelStrategy: (Long, Long) -> (Long)): Long {
        val monkeys = parseInput()
        val modulus = monkeys.map { it.test.divisibleBy }.reduce(Long::times)
        repeat(rounds) {
            monkeys.forEach { monkey ->
                val (dividedByZero, notDividedByZero) = monkey.items
                    .map { worryLevelStrategy(monkey.inspect(it), modulus) }
                    .partition { (it % monkey.test.divisibleBy == 0L) }
                monkeys[monkey.test.monkeyTrue].items.addAll(dividedByZero)
                monkeys[monkey.test.monkeyFalse].items.addAll(notDividedByZero)
                monkey.items.clear()
            }
        }
        return monkeys.map { it.inspected.toLong() }.sortedDescending().let { it[0] * it[1] }
    }

    private fun parseInput(): List<Monkey> = readInput("day11")
        .windowed(6, 7)
        .map { Monkey.from(it) }
}

data class Monkey(
    val items: MutableList<Long>,
    val operation: Operation,
    val test: Test,
    var inspected: Int = 0
) {

    fun inspect(item: Long): Long {
        inspected++
        return if (operation.operationType == "+") {
            item + (operation.value ?: item)
        } else {
            item * (operation.value ?: item)
        }
    }

    companion object {
        fun from(monkey: List<String>): Monkey =
            Monkey(
                items = monkey[1].substringAfter(": ").split(", ").map { it.toLong() }.toMutableList(),
                operation = monkey[2].substringAfter("= old ").split(" ")
                    .let { Operation(it[0], it[1].toLongOrNull()) },
                test = Test(
                    monkey[3].substringAfter("Test: divisible by ").toLong(),
                    monkey[4].substringAfter("If true: throw to monkey ").toInt(),
                    monkey[5].substringAfter("If false: throw to monkey ").toInt()
                )
            )
    }
}

data class Operation(val operationType: String, val value: Long?)

data class Test(val divisibleBy: Long, val monkeyTrue: Int, val monkeyFalse: Int)

fun main() {
    println(Day11.part1())
    println(Day11.part2())
}
