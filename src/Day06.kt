fun main() {
    fun parseInputPart1(text: String): List<Pair<Long, Long>> {
        val (times, distances) = text.split("\n").map { it.substringAfter(":").trim().split("""\s+""".toRegex()) }
        return times.zip(distances).map { (t, d) -> t.toLong() to d.toLong() }
    }

    fun parseInputPart2(text: String): List<Pair<Long, Long>> {
        val (time, distance) = text.split("\n").map { it.substringAfter(":").replace("\\s+".toRegex(), "") }
        return listOf(time.toLong() to distance.toLong())
    }

    fun race(input: List<Pair<Long, Long>>): Int {
        val res = mutableListOf<Int>()
        for ((time, recordDistance) in input) {
            var waysToWin = 0
            for (i in 0..time) {
                if (i * (time - i) > recordDistance) {
                    waysToWin++
                }
            }
            res.add(waysToWin)
        }
        return res.reduce { acc, i -> acc * i }
    }

    // test if implementation meets criteria from the description, like:
    val testText = readText("test")
    val testInputPart1 = parseInputPart1(testText)
    check(race(testInputPart1) == 288)
    val testInputPart2 = parseInputPart2(testText)
    check(race(testInputPart2) == 71503)

    val realText = readText("real")
    val realInputPart1 = parseInputPart1(realText)
    race(realInputPart1).println()
    val realInputPart2 = parseInputPart2(realText)
    race(realInputPart2).println()
}
