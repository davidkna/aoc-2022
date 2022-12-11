fun main() {
    fun part1(input: List<List<String>>): Int = input.maxOf { l -> l.sumOf { n -> n.toInt() } }

    fun part2(input: List<List<String>>): Int {
        val max = intArrayOf(0, 0, 0)
        input.map { l -> l.sumOf { n -> n.toInt() } }.forEach { n ->
            if (n > max[0]) {
                max[0] = n
                max.sort()
            }
        }
        return max.sum()
    }

    val testInput = readInputDoubleNewline("Day01_test")
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = readInputDoubleNewline("Day01")
    println(part1(input))
    println(part2(input))
}
