fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val first = line.first { it.isDigit() }
            val last = line.last { it.isDigit() }
            "$first$last".toInt()
        }
    }

    val wordToDigit = mapOf(
        "one" to '1', "two" to '2', "three" to '3',
        "four" to '4', "five" to '5', "six" to '6',
        "seven" to '7', "eight" to '8', "nine" to '9',
    )

    fun containsDigit(line: String, idx: Int): Char? {
        if (line[idx].isDigit()) return line[idx]
        return wordToDigit.entries.firstOrNull { (word, _) ->
            line.startsWith(word, idx)
        }?.value
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            val first = line.indices.firstNotNullOf { containsDigit(line, it) }
            val last = line.indices.reversed().firstNotNullOf { containsDigit(line, it) }
            "$first$last".toInt()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part2(testInput) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
