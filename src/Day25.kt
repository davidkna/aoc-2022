fun main() {
    fun part1(input: List<String>): String {
        var n = input.sumOf {
            it.fold(0L) { acc, c ->
                acc * 5L + when (c) {
                    '0' -> 0L
                    '1' -> 1L
                    '2' -> 2L
                    '-' -> -1L
                    '=' -> -2L
                    else -> throw IllegalArgumentException("Invalid input $c")
                }
            }
        }
        val s = ArrayDeque<Int>()
        var acc = 0
        while (n != 0L) {
            var rem = (n % 5L).toInt() + acc
            acc = if (rem >= 3) {
                rem -= 5
                1
            } else {
                0
            }
            s.add(rem)
            n /= 5
        }
        return s.reversed().joinToString("") {
            when (it) {
                0 -> "0"
                1 -> "1"
                2 -> "2"
                -1 -> "-"
                -2 -> "="
                else -> throw IllegalArgumentException("Invalid input $it")
            }
        }
    }

    val testInput = readInput("Day25_test")
    check(part1(testInput) == "2=-1=0")

    val input = readInput("Day25")
    println(part1(input))
}
