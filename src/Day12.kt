object Day12 {

    fun part1(): Int {
        val graph = readGraph()
        val startAt = graph.first { it.value == 'S' }
        val queue = mutableListOf(startAt.coordinate)
        val visited = mutableSetOf(startAt.coordinate)
        val results = graph.map { it.coordinate }.associateWith { 0 }.toMutableMap()

        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()
            val currentNodeResult = results[node]!!

            if (graph.nodeByCoordinate(node).adjacentNodes.any { graph.nodeByCoordinate(it).value == 'E' }) {
                return currentNodeResult + 1
            }

            val adjacentNodes = graph.nodeByCoordinate(node).adjacentNodes
            adjacentNodes
                .filter { it !in visited }
                .also { queue.addAll(it) }
                .forEach { results[it] = currentNodeResult + 1 }
            visited.addAll(adjacentNodes)
        }
        return 0
    }

    fun part2(): Int {
        val graph = readGraph()
        val startingPositions = graph.filter { it.value in setOf('a', 'S') }.map { it.coordinate }
        val pathLengths = mutableSetOf<Int>()
        startingPositions.forEach { startingPosition ->
            val startAt = graph.first { it.coordinate == startingPosition }
            val queue = mutableListOf(startAt.coordinate)
            val queued = mutableSetOf(startAt.coordinate)
            val nodePathLengths = graph.map { it.coordinate }.associateWith { 0 }.toMutableMap()

            while (queue.isNotEmpty()) {
                val node = queue.removeFirst()
                val currentNodeResult = nodePathLengths[node]!!

                if (graph.nodeByCoordinate(node).adjacentNodes.any { graph.nodeByCoordinate(it).value == 'E' }) {
                    pathLengths.add(currentNodeResult + 1)
                    break
                }

                val adjacentNodes = graph.nodeByCoordinate(node).adjacentNodes
                adjacentNodes
                    .filter { it !in queued }
                    .also { queue.addAll(it) }
                    .forEach { nodePathLengths[it] = currentNodeResult + 1 }
                queued.addAll(adjacentNodes)
            }
        }
        return pathLengths.min()
    }

    private fun readGraph(): List<HillNode> {
        val nodes = readInput("day12")
            .map { line -> line.chunked(1).map { it.toCharArray()[0] } }
            .let { nodes ->
                val width = nodes[0].size
                nodes.flatten()
                    .mapIndexed { index, value -> HillNode(value, NodeCoordinate(index % width, index / width)) }
            }
        nodes.forEach { nodes.findAdjacentNodes(it) }
        return nodes
    }
}

private fun List<HillNode>.nodeByCoordinate(coordinate: NodeCoordinate): HillNode =
    first { it.coordinate == coordinate }

private fun List<HillNode>.findAdjacentNodes(node: HillNode) {
    val left = find { it.coordinate == NodeCoordinate(node.coordinate.x - 1, node.coordinate.y) }
    val right = find { it.coordinate == NodeCoordinate(node.coordinate.x + 1, node.coordinate.y) }
    val top = find { it.coordinate == NodeCoordinate(node.coordinate.x, node.coordinate.y - 1) }
    val bottom = find { it.coordinate == NodeCoordinate(node.coordinate.x, node.coordinate.y + 1) }

    listOfNotNull(left, right, top, bottom)
        .filter { it.value.determineChar().code <= node.value.determineChar().code + 1 }
        .map { it.coordinate }
        .also { node.adjacentNodes.addAll(it) }
}

private fun Char.determineChar(): Char =
    when (this) {
        'E' -> 'z'
        'S' -> 'a'
        else -> this
    }

data class HillNode(
    val value: Char,
    val coordinate: NodeCoordinate,
    val adjacentNodes: MutableList<NodeCoordinate> = mutableListOf()
)

data class NodeCoordinate(val x: Int, val y: Int)

fun main() {
    println(Day12.part1())
    println(Day12.part2())
}