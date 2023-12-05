fun main() {
    fun parseInput(input: List<String>): List<Pair<Set<Int>, Set<Int>>> {
        val cards = mutableListOf<Pair<Set<Int>, Set<Int>>>()
        for (line in input) {
            val (_, numbers) = line.split(": ")
            val (winningNumbers, myNumbers) = numbers.split(" | ")
            cards.add(
                winningNumbers.split(" ").mapNotNull { it.toIntOrNull() }.toSet() to
                        myNumbers.split(" ").mapNotNull { it.toIntOrNull() }.toSet()
            )
        }
        return cards
    }

    fun part1(cards: List<Pair<Set<Int>, Set<Int>>>): Int {
        var totalPoints = 0
        for (card in cards) {
            val matching = card.first.intersect(card.second)
            totalPoints += if (matching.isNotEmpty()) 1 shl matching.size - 1 else 0
        }
        return totalPoints
    }

    fun part2(cards: List<Pair<Set<Int>, Set<Int>>>): Int {
        val wonCards = cards.indices.associateWith { 1 }.toMutableMap()
        for ((cardNumber, card) in cards.withIndex()) {
            val numMatching = card.first.intersect(card.second).size
            for (i in cardNumber + 1 .. cardNumber + numMatching) {
                wonCards[i] = wonCards.getOrDefault(i, 0) + wonCards.getOrDefault(cardNumber, 0)
            }
        }
        return wonCards.values.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("test")
    val testNumbers = parseInput(testInput)
    check(part1(testNumbers) == 13)
    check(part2(testNumbers) == 30)

    val input = readInput("real")
    val realNumbers = parseInput(input)
    part1(realNumbers).println()
    part2(realNumbers).println()
}
