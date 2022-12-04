fun main() {
    fun part1(input: List<String>): Int {
        var max = 0
        var current = 0
        for (line in input) {
            if (line.isEmpty()) {
                max = max.coerceAtLeast(current)
                current = 0
                continue
            }
            val value = Integer.parseInt(line)
            current += value
        }
        return max.coerceAtLeast(current)
    }

    // Find top 3
    fun part2(input: List<String>): Int {
        var max = arrayListOf(0, 0, 0)
        var current = 0
        for (line in input) {
            if (line.isEmpty()) {
                if (current > max[0]) {
                    max[0] = current
                    max.sort()
                }

                current = 0
                continue
            }
            val value = Integer.parseInt(line)
            current += value
        }
        if (current > max[0]) {
            max[0] = current
        }
        return max.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
