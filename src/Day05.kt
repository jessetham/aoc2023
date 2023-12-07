import kotlin.math.min
import kotlin.math.sign

fun main() {
    data class Range(val source: Long, val destination: Long, val length: Long)

    fun parseSeeds(line: String) = line.substringAfter("seeds: ").split(" ").map { it.toLong() }

    fun parseMaps(lines: List<String>): List<List<Range>> {
        val maps = mutableListOf<List<Range>>()
        for (line in lines) {
            val map = mutableListOf<Range>()
            for (m in line.split("\n").drop(1)) {
                val (destination, source, length) = m.split(" ").map { it.toLong() }
                map.add(Range(source, destination, length))
            }
            map.sortBy { it.source }
            maps.add(map)
        }
        return maps
    }

    fun parseInput(input: String): Pair<List<Long>, List<List<Range>>> {
        val lines = input.split("\n\n")
        val seeds = parseSeeds(lines.first())
        val maps = parseMaps(lines.drop(1))
        return seeds to maps
    }

    fun part1(seeds: List<Long>, maps: List<List<Range>>): Long {
        var lowestSoFar = Long.MAX_VALUE
        for (seed in seeds) {
            var current = seed
            for (map in maps) {
                var idx = map.binarySearch { (it.source - current).sign }
                if (idx < 0)
                    idx = -(idx + 1) - 1
                if (idx >= 0) {
                    val range = map[idx]
                    if (current in range.source until range.source + range.length)
                        current = range.destination + (current - range.source)
                }
            }
            lowestSoFar = min(lowestSoFar, current)
        }
        return lowestSoFar
    }

    // Pretty slow, there's probably a way to optimize this
    fun part2(seeds: List<Long>, maps: List<List<Range>>): Long {
        var minSoFar = Long.MAX_VALUE
        for (range in seeds.chunked(2)) {
            for (delta in 0 until range[1]) {
                minSoFar = min(minSoFar, part1(listOf(range[0] + delta), maps))
            }
        }
        return minSoFar
    }

    // test if implementation meets criteria from the description, like:
    val testText = readText("test")
    val testInput = parseInput(testText)
    check(part1(testInput.first, testInput.second) == 35L)
    check(part2(testInput.first, testInput.second) == 46L)

    val realText = readText("real")
    val realInput = parseInput(realText)
    part1(realInput.first, realInput.second).println()
    part2(realInput.first, realInput.second).println()
}
