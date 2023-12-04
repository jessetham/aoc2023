fun main() {
    fun parseRounds(game: String): List<HashMap<String, Int>> {
        val cubeRegex = """(\d+) ([a-zA-Z]+)""".toRegex()
        val rounds = mutableListOf<HashMap<String, Int>>()
        for (round in game.split("; ")) {
            val seenCubes = hashMapOf<String, Int>()
            for (cubes in round.split(", ")) {
                val (count, color) = cubeRegex.find(cubes)!!.destructured
                seenCubes[color] = seenCubes.getOrDefault(color, 0) + count.toInt()
            }
            rounds.add(seenCubes)
        }
        return rounds
    }

    fun parseInput(input: List<String>): List<Pair<Int, List<HashMap<String, Int>>>> {
        val games = mutableListOf<Pair<Int, List<HashMap<String, Int>>>>()
        val gameRegex = """Game (\d+)""".toRegex()

        for (line in input) {
            val (gameString, roundsString) = line.split(": ")

            val gameMatch = gameRegex.find(gameString)
            val gameId = gameMatch!!.groupValues[1].toInt()
            val rounds = parseRounds(roundsString)
            games.add(gameId to rounds)
        }

        return games
    }

    fun part1(games: List<Pair<Int, List<HashMap<String, Int>>>>): Int {
        val availableCubes = mapOf("red" to 12, "green" to 13, "blue" to 14)
        var sum = 0
        for (game in games) {
            val (gameId, rounds) = game
            val isPossible = rounds.all { round ->
                availableCubes.all { (color, maxCubes) ->
                    round.getOrDefault(color, 0) <= maxCubes
                }
            }
            if (isPossible) sum += gameId
        }
        return sum
    }

    fun part2(games: List<Pair<Int, List<HashMap<String, Int>>>>): Int {
        val cubeColors = listOf("red", "green", "blue")
        var sum = 0
        for (game in games) {
            val (_, rounds) = game
            sum += cubeColors.fold(1) { acc, color ->
                acc * rounds.maxOf { it.getOrDefault(color, 0) }
            }
        }
        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("test")
    val testGames = parseInput(testInput)
    check(part1(testGames) == 8)
    check(part2(testGames) == 2286)

    val input = readInput("real")
    val realGames = parseInput(input)
    part1(realGames).println()
    part2(realGames).println()
}
