fun main() {
    fun parseText(text: String): Pair<String, Map<String, Pair<String, String>>> {
        val nodeRegex = """(\w+) = \((\w+), (\w+)\)""".toRegex()
        val (instructions, nodesText) = text.split("\n\n")
        val graph = mutableMapOf<String, Pair<String, String>>()
        for (nodeText in nodesText.split("\n")) {
            val (node, left, right) = nodeRegex.find(nodeText)!!.destructured
            graph[node] = left to right
        }
        return instructions to graph
    }

    fun countSteps(instructions: String, graph: Map<String, Pair<String, String>>, start: String, end: String): Int {
        var steps = 0
        var node = start
        while (!node.endsWith(end)) {
            node = if (instructions[steps % instructions.length] == 'L') {
                graph[node]!!.first
            } else {
                graph[node]!!.second
            }
            steps++
        }
        return steps
    }

    fun gcd(a: Long, b: Long): Long {
        if (b == 0L)
            return a
        return gcd(b, a % b)
    }

    fun lcm(a: Long, b: Long): Long {
        val gcdRes = gcd(a, b)
        return (a * b) / gcdRes
    }

    fun part2(instructions: String, graph: Map<String, Pair<String, String>>): Long {
        val starts = graph.keys.filter { it.endsWith("A") }
        val steps = starts.map { start -> countSteps(instructions, graph, start, "Z").toLong() }
        return steps.reduce(::lcm)
    }

    val testText = readText("test")
    val (testInstructions, testGraph) = parseText(testText)
    // check(countSteps(testInstructions, testGraph, "AAA", "ZZZ") == 6)
    check(part2(testInstructions, testGraph) == 6L)

    val realText = readText("real")
    val (realInstructions, realGraph) = parseText(realText)
    // countSteps(realInstructions, realGraph, "AAA", "ZZZ").println()
    part2(realInstructions, realGraph).println()
}
