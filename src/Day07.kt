object Day07 {

    private const val MAX_SIZE = 100_000
    private const val HARD_DRIVE_SIZE = 70_000_000
    private const val SPACE_NEEDED = 30_000_000

    fun part1() = getRoot()
        .getChildrenSizes()
        .filter { it <= MAX_SIZE }
        .sum()

    fun part2() = getRoot()
        .getChildrenSizes()
        .sorted()
        .let { sizes ->
            val spaceTaken = sizes.max()
            sizes.first { spaceTaken + SPACE_NEEDED - HARD_DRIVE_SIZE <= it }
        }

    private fun getRoot(): Node = readInput("day07")
        .map(::TerminalCommand)
        .let { lines ->
            val directoryRoot = Node.createEmptyNode("/", null)
            var currentDirectory = directoryRoot
            lines.forEach { command ->
                when {
                    command.isDirectoryChangeToRoot() ->
                        currentDirectory = directoryRoot

                    command.isDirectoryChangeToParent() ->
                        currentDirectory = currentDirectory.parent ?: throw IllegalStateException("Batman")

                    command.isDirectoryChangeToChild() -> currentDirectory =
                        currentDirectory.findChild(command.line.substring(5))

                    command.isDirectoryListed() ->
                        currentDirectory.addChild(command.toEmptyNode(currentDirectory))

                    command.isFileListed() ->
                        currentDirectory.addFile(command.toFile())
                }
            }
            directoryRoot
        }
}

data class TerminalCommand(val line: String) {

    fun isDirectoryChangeToRoot(): Boolean = line == "$ cd /"

    fun isDirectoryChangeToParent(): Boolean = line == "$ cd .."

    fun isDirectoryChangeToChild(): Boolean = line.startsWith("$ cd ")

    fun isDirectoryListed(): Boolean = line.startsWith("dir")

    fun isFileListed(): Boolean = line.first().digitToIntOrNull() != null

    fun toEmptyNode(parent: Node?): Node = Node.createEmptyNode(line.substring(4), parent)

    fun toFile(): File = line.split(" ").let { File(it[0].toInt(), it[1]) }
}

data class Node(
    val name: String,
    val parent: Node?,
    val children: MutableList<Node>,
    val files: MutableList<File>
) {
    fun getChildrenSizes(): List<Int> =
        mutableListOf(size()).let { nodeSize -> nodeSize + children.flatMap { it.getChildrenSizes() } }

    fun findChild(name: String): Node = children.first { it.name == name }

    fun addChild(node: Node) = children.add(node)

    fun addFile(file: File) = files.add(file)

    private fun size(): Int = children.sumOf { it.size() } + filesSize()

    private fun filesSize(): Int = files.sumOf { it.size }

    companion object {
        fun createEmptyNode(name: String, parent: Node?): Node = Node(name, parent, mutableListOf(), mutableListOf())
    }
}

data class File(val size: Int, val name: String)

fun main() {
    println(Day07.part1())
    println(Day07.part2())
}