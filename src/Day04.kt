fun main() {
    fun Boolean.toInt() = if (this) 1 else 0

    fun part1(input: List<String>): Int {
        return input.filter { return@filter it.isNotEmpty() }.sumOf {
            val (first, second) = it.split(",")
            val (x0, y0) = first.split("-").map { n -> n.toInt() }
            val (x1, y1) = second.split("-").map { n -> n.toInt() }

            (
                (
                    (x0..y0).contains(x1) &&
                        (x0..y0).contains(y1)
                    ) ||
                    (
                        (x1..y1).contains(x0) &&
                            (x1..y1).contains(y0)
                        )
                ).toInt()
        }
    }

    fun part2(input: List<String>): Int {
        return input.filter { return@filter it.isNotEmpty() }.sumOf {
            val (first, second) = it.split(",")
            val (x0, y0) = first.split("-").map { n -> n.toInt() }
            val (x1, y1) = second.split("-").map { n -> n.toInt() }

            (
                (x0..y0).contains(x1) ||
                    (x0..y0).contains(y1) ||
                    (x1..y1).contains(x0) ||
                    (x1..y1).contains(y0)
                ).toInt()
        }
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
