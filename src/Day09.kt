fun main() {
    fun parseInput(lines: List<String>) = lines.map { line -> line.split(" ").map { it.toLong() } }

    fun getReductions(data: List<Long>): List<List<Long>> {
        val reductions = mutableListOf(data)
        while (!reductions.last().all { it == 0L }) {
            val newReduction = reductions.last().windowed(2).map { it[1] - it[0] }
            reductions.add(newReduction)
        }
        return reductions
    }

    fun extrapolateForwards(data: List<Long>) =
        getReductions(data).reversed().fold(0L) { acc, reduction -> acc + reduction.last() }

    fun extrapolateBackwards(data: List<Long>) =
        getReductions(data).reversed().fold(0L) { acc, reduction -> reduction.first() - acc }

    val testInput = parseInput(readLines("test"))
    check(testInput.sumOf { extrapolateForwards(it) } == 114L)
    check(testInput.sumOf { extrapolateBackwards(it) } == 2L)

    val realInput = parseInput(readLines("real"))
    realInput.sumOf { extrapolateForwards(it) }.println()
    realInput.sumOf { extrapolateBackwards(it) }.println()
}
