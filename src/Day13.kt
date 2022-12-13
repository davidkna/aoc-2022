fun main() {
    fun parseToken(token: String): Pair<Int, Int> {
        var head = 0
        var tail = token.length

        for (c in token) {
            when (c) {
                '[' -> head++
                ']' -> tail--
            }
        }
        return when (head) {
            tail -> Pair(0, -1)
            else -> Pair(tail - head, token.substring(head, tail).toInt())
        }
    }

    fun compareTokens(aTokens: List<Pair<Int, Int>>, bTokens: List<Pair<Int, Int>>): Boolean {
        var aDepth = 0
        var bDepth = 0

        for ((aToken, bToken) in aTokens.zip(bTokens)) {
            when {
                aDepth > bDepth -> return false
                aDepth < bDepth -> return true
                aToken.second > bToken.second -> return false
                aToken.second < bToken.second -> return true
            }

            aDepth += aToken.first
            bDepth += bToken.first
        }

        return when {
            aDepth > bDepth -> false
            aDepth < bDepth -> true
            else -> aTokens.size < bTokens.size
        }
    }

    fun part1(input: List<List<String>>): Int {
        return input.withIndex().filter { (_, tokenStrings) ->
            val aTokens = tokenStrings[0].split(",").map { parseToken(it) }
            val bTokens = tokenStrings[1].split(",").map { parseToken(it) }

            compareTokens(aTokens, bTokens)
        }.sumOf { it.index + 1 }
    }

    fun part2(input: List<String>): Int {
        val startToken = listOf(parseToken("[[2]]"))
        val endToken = listOf(parseToken("[[6]]"))

        val tokens = input
            .filter { it.isNotEmpty() }
            .map { tl -> tl.split(",").map { t -> parseToken(t) }.toList() }

        var start = 1
        var end = tokens.size + 2

        tokens.forEach {
            if (compareTokens(it, startToken)) {
                start++
            } else if (compareTokens(endToken, it)) {
                end--
            }
        }

        return start * end
    }

    val testInput = readInputDoubleNewline("Day13_test")
    check(part1(testInput) == 13)
    val testInput2 = readInput("Day13_test")
    check(part2(testInput2) == 140)

    val input = readInputDoubleNewline("Day13")
    println(part1(input))
    val input2 = readInput("Day13")
    println(part2(input2))
}
