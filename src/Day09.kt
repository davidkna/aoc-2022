import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sign

fun main() {
    fun parseInput(input: List<String>): List<Pair<Int, Int>> {
        return input.map { l ->
                val (direction, steps) = l.split(" ")
                val stepsInt = steps.toInt()
                return@map when (direction) {
                    "R" -> Pair(stepsInt, 0)
                    "L" -> Pair(-stepsInt, 0)
                    "U" -> Pair(0, stepsInt)
                    "D" -> Pair(0, -stepsInt)
                    else -> throw Exception("Unknown direction")
                }
            }.toList()
    }

    fun solution(input: List<String>, len: Int): Int {
        val rope = Array(len + 1) { Pair(0, 0) }

        val moves = parseInput(input)
        val visited = mutableSetOf<Pair<Int, Int>>()
        visited.add(Pair(0, 0))

        for (move in moves) {
            (0 until max(abs(move.first), abs(move.second))).forEach { _ ->
                rope[0] = Pair(rope[0].first + move.first.sign, rope[0].second + move.second.sign)

                rope.indices.drop(1).forEach { i ->
                    val head = rope[i - 1]
                    val tail = rope[i]
                    if (abs(head.first - tail.first) > 1 || abs(head.second - tail.second) > 1) {
                        val xStep = (head.first - tail.first).sign
                        val yStep = (head.second - tail.second).sign
                        rope[i] = Pair(tail.first + xStep, tail.second + yStep)
                    }
                }
                visited.add(rope.last())
            }
        }
        return visited.size
    }

    val testInputA = readInput("Day09_test_a")
    val testInputB = readInput("Day09_test_b")
    check(solution(testInputA, 1) == 13)
    check(solution(testInputA, 9) == 1)
    check(solution(testInputB, 9) == 36)

    val input = readInput("Day09")
    println(solution(input, 1))
    println(solution(input, 9))
}
