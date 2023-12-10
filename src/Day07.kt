fun main() {
    data class Hand(val cards: String, val bet: Int) {
        val countedCards = cards.groupingBy { it }.eachCount()

        fun isFiveOfAKind() = this.countedCards.size == 1
        fun isFourOfAKind() = this.countedCards.values.any { it == 4 }
        fun isFullHouse() = this.countedCards.size == 2 && this.isThreeOfAKind() && this.isOnePair()
        fun isThreeOfAKind() = this.countedCards.values.any { it == 3 }
        fun isTwoPair() = this.countedCards.values.count { it == 2 } == 2
        fun isOnePair() = this.countedCards.values.any { it == 2 }

        fun canBeFiveOfAKind() =
            this.countedCards.filterKeys { it != 'J' }.any { it.value + this.countedCards.getOrDefault('J', 0) == 5 } ||
                    this.countedCards.filterKeys { it != 'J' }.isEmpty()

        fun canBeFourOfAKind() =
            this.countedCards.filterKeys { it != 'J' }.any { it.value + this.countedCards.getOrDefault('J', 0) >= 4 } ||
                    this.countedCards.filterKeys { it != 'J' }.isEmpty()

        fun canBeFullHouse() =
            this.countedCards.filterKeys { it != 'J' }.let { it.size <= 2 && it.all { count -> count.value <= 3 } }

        fun canBeThreeOfAKind() =
            this.countedCards.filterKeys { it != 'J' }.any { it.value + this.countedCards.getOrDefault('J', 0) >= 3 } ||
                    this.countedCards.filterKeys { it != 'J' }.isEmpty()

        fun canBeTwoPair(): Boolean {
            var numJokers = this.countedCards.getOrDefault('J', 0)
            var numPairs = 0
            for (count in this.countedCards.filterKeys { it != 'J' }.values.sortedDescending()) {
                if (count >= 2) {
                    numPairs++
                } else if (count == 1 && numJokers > 0) {
                    numPairs++
                    numJokers--
                } else if (numJokers > 1) {
                    numPairs++
                    numJokers -= 2
                }
                if (numPairs == 2)
                    break
            }
            return numPairs == 2 || numPairs == 1 && numJokers >= 2 || numPairs == 0 && numJokers >= 4
        }

        fun canBeOnePair() =
            this.countedCards.filterKeys { it != 'J' }.any { it.value + this.countedCards.getOrDefault('J', 0) >= 2 } ||
                    this.countedCards.filterKeys { it != 'J' }.isEmpty()

        fun getType() = when {
            this.isFiveOfAKind() -> 7
            this.isFourOfAKind() -> 6
            this.isFullHouse() -> 5
            this.isThreeOfAKind() -> 4
            this.isTwoPair() -> 3
            this.isOnePair() -> 2
            else -> 1
        }

        fun getTypeWithJoker() = when {
            this.canBeFiveOfAKind() -> 7
            this.canBeFourOfAKind() -> 6
            this.canBeFullHouse() -> 5
            this.canBeThreeOfAKind() -> 4
            this.canBeTwoPair() -> 3
            this.canBeOnePair() -> 2
            else -> 1
        }
    }

    val normalComparator = Comparator<Hand> { o1, o2 ->
        val res = o1.getType() - o2.getType()
        if (res != 0)
            return@Comparator res

        // tiebreaker
        val possibleCards = "23456789TJQKA"
        for ((a, b) in o1.cards.zip(o2.cards)) {
            val aIdx = possibleCards.indexOf(a)
            val bIdx = possibleCards.indexOf(b)
            if (aIdx != bIdx)
                return@Comparator aIdx - bIdx
        }

        throw Exception("unable to break tie")
    }

    val jokerComparator = Comparator<Hand> { o1, o2 ->
        val res = o1.getTypeWithJoker() - o2.getTypeWithJoker()
        if (res != 0)
            return@Comparator res

        // tiebreaker
        val possibleCards = "J23456789TQKA"
        for ((a, b) in o1.cards.zip(o2.cards)) {
            val aIdx = possibleCards.indexOf(a)
            val bIdx = possibleCards.indexOf(b)
            if (aIdx != bIdx)
                return@Comparator aIdx - bIdx
        }

        throw Exception("unable to break tie")
    }

    fun parseInput(lines: List<String>) = lines.map { it.split(' ').let { split -> Hand(split[0], split[1].toInt()) } }

    fun calculateScore(input: List<Hand>, comparator: Comparator<Hand>): Int {
        return input.sortedWith(comparator).withIndex().fold(0) { acc, (i, hand) -> acc + (i + 1) * hand.bet }
    }

    // test if implementation meets criteria from the description, like:
    val testLines = readLines("test")
    val testInput = parseInput(testLines)
    check(calculateScore(testInput, normalComparator) == 6440)
    check(calculateScore(testInput, jokerComparator) == 5905)

    val realLines = readLines("real")
    val realInput = parseInput(realLines)
    calculateScore(realInput, normalComparator).println()
    calculateScore(realInput, jokerComparator).println()
}
