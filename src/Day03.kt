fun main() {
    fun List<String>.isPartNumber(r: Int, c: Int): Boolean {
        val deltas = listOf(
            -1 to 1, 0 to 1, 1 to 1, -1 to 0, 1 to 0, -1 to -1, 0 to -1, 1 to -1
        )
        return deltas.any { (dr, dc) ->
            this.getOrNull(r + dr)?.getOrNull(c + dc)?.let { !it.isDigit() && it != '.' } == true
        }
    }

    fun part1(grid: List<String>): Int {
        var sum = 0
        for ((h, row) in grid.withIndex()) {
            var r = 0
            while (r < row.length) {
                if (!row[r].isDigit()) {
                    r++
                    continue
                }
                val start = r
                while (r < row.length && row[r].isDigit()) r++
                if ((start until r).any { grid.isPartNumber(h, it) }) {
                    sum += row.substring(start, r).toInt()
                }
            }
        }
        return sum
    }

    fun String.getPartNumber(c: Int, visited: MutableSet<Int>): Int {
        var start = c
        var end = c
        visited.add(c)
        while (start - 1 >= 0 && this[start - 1].isDigit()) {
            start--
            visited.add(start)
        }
        while (end + 1 < this.length && this[end + 1].isDigit()) {
            end++
            visited.add(end)
        }
        return this.substring(start..end).toInt()
    }

    fun List<String>.getPartNumbers(r: Int, c: Int): List<Int> {
        val partNumbers = mutableListOf<Int>()
        for (nr in r - 1..r + 1) {
            if (nr !in indices) continue
            val visited = mutableSetOf<Int>()
            val row = this[nr]
            for (nc in c - 1..c + 1) {
                if (nc in row.indices && row[nc].isDigit() && nc !in visited) partNumbers.add(
                    row.getPartNumber(
                        nc, visited
                    )
                )
            }
        }
        return partNumbers
    }

    fun part2(grid: List<String>): Long {
        var sum = 0L
        for ((r, row) in grid.withIndex()) {
            for (c in row.indices) {
                if (row[c] == '*') {
                    val partNumbers = grid.getPartNumbers(r, c)
                    if (partNumbers.size == 2) sum += partNumbers[0] * partNumbers[1]
                }
            }
        }
        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835L)

    val input = readInput("real")
    part1(input).println()
    part2(input).println()
}
