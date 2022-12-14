import kotlin.math.max
import kotlin.math.min
fun main() {
    fun solution(input: List<String>, part2: Boolean): Int {
        val map = HashSet<Pair<Int, Int>>()

        input.forEach { line ->
            line.split(" -> ").windowed(2).forEach { (a, b) ->
                val (x0, y0) = a.split(",").map { it.toInt() }
                val (x1, y1) = b.split(",").map { it.toInt() }

                for (x in min(x0, x1)..max(x0, x1)) {
                    for (y in min(y0, y1)..max(y0, y1)) {
                        map.add(Pair(x, y))
                    }
                }
            }
        }

        val minX = map.minBy { it.first }.first
        val maxX = map.maxBy { it.first }.first
        val maxY = map.maxBy { it.second }.second

        var i = 0
        outer@while (true) {
            var sand = Pair(500, 0)

            inner@while (true) {
                when {
                    !part2 && (!(minX..maxX).contains(sand.first) || sand.second > maxY) -> break@outer
                    part2 && sand.second == maxY + 1 -> break@inner
                    !map.contains(Pair(sand.first, sand.second + 1)) -> {
                        sand = Pair(sand.first, sand.second + 1)
                    }
                    !map.contains(Pair(sand.first - 1, sand.second + 1)) -> {
                        sand = Pair(sand.first - 1, sand.second + 1)
                    }
                    !map.contains(Pair(sand.first + 1, sand.second + 1)) -> {
                        sand = Pair(sand.first + 1, sand.second + 1)
                    }
                    part2 && sand == Pair(500, 0) -> {
                        i++
                        break@outer
                    }
                    else -> break@inner
                }
            }
            map.add(sand)
            i++
        }

        return i
    }

    fun part1(input: List<String>) = solution(input, false)
    fun part2(input: List<String>) = solution(input, true)

    val testInput = readInput("Day14_test")
    check(part1(testInput) == 24)
    check(part2(testInput) == 93)

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}
