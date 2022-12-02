class Day02 {
    private val rules: Map<String, Rules> = mapOf(
        "A" to Rules("Y", "Z", "X"),
        "B" to Rules("Z", "X", "Y"),
        "C" to Rules("X", "Y", "Z")
    )

    private val points: Map<String, Int> = mapOf(
        "X" to 1,
        "Y" to 2,
        "Z" to 3,
    )

    fun part1() = parseData()
        .sumOf {
            when {
                isWin(it) -> 6 + points[it.player]!!
                isDraw(it) -> 3 + points[it.player]!!
                else -> points[it.player]!!
            }
        }

    fun part2() = parseData()
        .sumOf { match ->
            when (match.player) {
                "X" -> rules[match.opponent]!!.loses.let { points[it] }!!
                "Y" -> rules[match.opponent]!!.draws.let { points[it] }!! + 3
                else -> rules[match.opponent]!!.wins.let { points[it] }!! + 6
            }
        }

    private fun parseData() = readInput("day02").map { Match(it[0].toString(), it[2].toString()) }
    
    private fun isWin(match: Match): Boolean = rules[match.opponent]!!.wins == match.player

    private fun isDraw(match: Match): Boolean = rules[match.opponent]!!.draws == match.player
}

data class Match(val opponent: String, val player: String)

data class Rules(val wins: String, val loses: String, val draws: String)

fun main() {
    val day02 = Day02()
    println(day02.part1())
    println(day02.part2())
}