import kotlin.collections.ArrayDeque
fun main() {
    fun parseInput(input: List<String>) = input.map {
        val (x, y, z) = it.split(",").map { n -> n.toInt() }
        Triple(x, y, z)
    }.toList()

    val directions = listOf(
        Triple(1, 0, 0),
        Triple(-1, 0, 0),
        Triple(0, 1, 0),
        Triple(0, -1, 0),
        Triple(0, 0, 1),
        Triple(0, 0, -1),
    )

    fun part1(input: List<String>): Int {
        val cubes = parseInput(input)
        val map = hashSetOf(*cubes.toTypedArray())

        return cubes.flatMap { cube ->
            directions.map {
                Triple(
                    cube.first + it.first,
                    cube.second + it.second,
                    cube.third + it.third,
                )
            }
        }.count { it !in map }
    }

    fun part2(input: List<String>): Int {
        val cubes = parseInput(input)
        val map = hashSetOf(*cubes.toTypedArray())

        val maxX = cubes.maxBy { it.first }.first + 1
        val minX = cubes.minBy { it.first }.first - 1
        val maxY = cubes.maxBy { it.second }.second + 1
        val minY = cubes.minBy { it.second }.second - 1
        val maxZ = cubes.maxBy { it.third }.third + 1
        val minZ = cubes.minBy { it.third }.third - 1

        val start = Triple(minX, minY, minZ)
        val visited: HashSet<Triple<Int, Int, Int>> = hashSetOf()
        val q = ArrayDeque<Triple<Int, Int, Int>>()
        q.add(start)

        var out = 0
        while (q.isNotEmpty()) {
            val cube = q.removeFirst()
            if (cube in visited) continue
            visited.add(cube)
            directions.map {
                Triple(
                    cube.first + it.first,
                    cube.second + it.second,
                    cube.third + it.third,
                )
            }.forEach { next ->
                if (next.first in minX..maxX && next.second in minY..maxY && next.third in minZ..maxZ && next !in visited) {
                    if (next in map) {
                        out++
                    } else if (next !in map && next !in visited) {
                        q.add(next)
                    }
                }
            }
        }

        return out
    }

    val testInputA = readInput("Day18_test_a")
    val testInputB = readInput("Day18_test_b")
    check(part1(testInputA) == 10)
    check(part1(testInputB) == 64)
    check(part2(testInputB) == 58)

    val input = readInput("Day18")
    println(part1(input))
    println(part2(input))
}
