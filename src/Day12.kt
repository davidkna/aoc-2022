import java.util.PriorityQueue

fun main() {
    fun height(c: Char): Int {
        if (('a'..'z').contains(c)) {
            return c.code - 'a'.code
        }
        return when (c) {
            'S' -> 0
            'E' -> 25
            else -> throw Exception("Unknown char")
        }
    }

    fun neighbors(x: Int, y: Int, input: List<String>): List<Pair<Int, Int>> {
        val neighbors = mutableListOf<Pair<Int, Int>>()
        if (x > 0) {
            neighbors.add(Pair(x - 1, y))
        }
        if (x < input.size - 1) {
            neighbors.add(Pair(x + 1, y))
        }

        if (y > 0) {
            neighbors.add(Pair(x, y - 1))
        }
        if (y < input[0].length - 1) {
            neighbors.add(Pair(x, y + 1))
        }

        val minHeight = height(input[x][y])
        neighbors.removeIf { height(input[it.first][it.second]) + 1 < minHeight }
        return neighbors.toList()
    }

    fun solution(input: List<String>, finalStates: List<Char>): Int {
        var start = Pair(0, 0)
        val q = PriorityQueue<Pair<Int, Pair<Int, Int>>> { a, b -> a.first - b.first }
        val visited = mutableMapOf<Pair<Int, Int>, Int>()
        for (y in input.indices) {
            for (x in input[y].indices) {
                val c = input[y][x]
                if (c == 'E') {
                    start = Pair(y, x)
                    break
                }
            }
        }

        q.add(Pair(0, start))
        visited[start] = 0
        while (q.isNotEmpty()) {
            val current = q.poll()
            if (finalStates.contains(input[current.second.first][current.second.second])) {
                return current.first
            }

            for (neighbor in neighbors(current.second.first, current.second.second, input)) {
                val newCost = current.first + 1
                if (!visited.contains(neighbor) || newCost < visited[neighbor]!!) {
                    visited[neighbor] = newCost
                    q.add(Pair(newCost, neighbor))
                }
            }
        }
        throw Exception("No solution")
    }

    val testInput = readInput("Day12_test")
    check(solution(testInput, listOf('S')) == 31)
    check(solution(testInput, listOf('S', 'a')) == 29)

    val input = readInput("Day12")
    println(solution(input, listOf('S')))
    println(solution(input, listOf('S', 'a')))
}
