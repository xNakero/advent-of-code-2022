object Day08 {

    fun part1() = parseInput().filterTreesWithVisibility().count()

    fun part2() = parseInput().scenicScores().max()

    private fun parseInput(): Forest = readInput("day08")
        .let {
            Forest(it[0].length, it.joinToString("").chunked(1).map { e -> e.toInt() })
        }
}

data class Forest(val width: Int, val trees: Trees) {

    fun filterTreesWithVisibility() =
        trees.filterIndexed { index, _ ->
            isSideVisible(index, treesToTheLeft(index)) ||
                    isSideVisible(index, treesToTheRight(index)) ||
                    isSideVisible(index, treesAbove(index)) ||
                    isSideVisible(index, treesBelow(index))
        }

    fun scenicScores() =
        trees.mapIndexed { index, tree ->
            sideScenicScore(tree, treesToTheLeft(index).reversed()) *
                    sideScenicScore(tree, treesToTheRight(index)) *
                    sideScenicScore(tree, treesAbove(index).reversed()) *
                    sideScenicScore(tree, treesBelow(index))
        }

    private fun isSideVisible(index: Int, sideTrees: Trees) = sideTrees.none { it >= trees[index] }

    private fun sideScenicScore(tree: Int, sideTrees: Trees) = sideTrees
        .indexOfFirst { it >= tree }
        .let { if (it == -1) sideTrees.size else it + 1 }

    private fun treesToTheLeft(index: Int) = trees.subList(index - (index % width), index)

    private fun treesToTheRight(index: Int) = trees.subList(index + 1, index + (width - index % width))

    private fun treesAbove(index: Int) = trees.filterIndexed { i, _ -> i % width == index % width && i < index }

    private fun treesBelow(index: Int) = trees.filterIndexed { i, _ -> i % width == index % width && i > index }
}

private typealias Trees = List<Int>

fun main() {
    println(Day08.part1())
    println(Day08.part2())
}