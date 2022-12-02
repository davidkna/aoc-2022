fun main() {
    fun part1(input: List<String>): Int {
        return input.filter { return@filter it.isNotEmpty() }.sumOf {
            var myChoice = it[2].code - 'X'.code
            var win = when (it[0].code - 'A'.code) {
                (myChoice + 1) % 3 -> 0
                myChoice -> 3
                else -> 6
            }

            return@sumOf win + myChoice + 1
        }
    }

    fun part2(input: List<String>): Int {
        return input.filter { return@filter it.isNotEmpty() }.sumOf {
            var outcome = it[2].code - 'X'.code
            var opChoice = it[0].code - 'A'.code

            return@sumOf outcome * 3 + (opChoice + outcome + 2) % 3 + 1
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
