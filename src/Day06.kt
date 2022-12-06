fun main() {
    fun solution(input: List<String>, n: Int): Int {
        return input.filter { return@filter it.isNotEmpty() }.sumOf { l ->
            val (i, _) = l
                .windowed(n)
                .withIndex()
                .find { (_, w) ->
                    w.toSet().size == n
                }!!
            return@sumOf i + n
        }
    }

    val testInput = readInput("Day06_test")
    check(solution(testInput, 4) == 7 + 5 + 6 + 10 + 11)
    check(solution(testInput, 14) == 19 + 23 + 23 + 29 + 26)

    val input = readInput("Day06")
    println(solution(input, 4))
    println(solution(input, 14))
}
