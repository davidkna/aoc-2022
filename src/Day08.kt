import kotlin.math.max

fun main() {
    // Convert to digits
    fun parseInput(input: List<String>): MutableList<MutableList<Int>> {
        return input
            .map {
                it
                    .map { n -> n.toString().toInt() }
                    .toMutableList()
            }.toMutableList()
    }

    fun part1(input: List<String>): Int {
        var counter = 0
        val grid = parseInput(input)

        val gridHorizontal = parseInput(input)
        for (y in 1..gridHorizontal.size - 2) {
            var left = 1
            var right = gridHorizontal[y].size - 2
            var leftHeight = gridHorizontal[y][0]
            var rightHeight = gridHorizontal[y][gridHorizontal[y].size - 1]

            for (x in 1..gridHorizontal[y].size - 2) {
                if (leftHeight < rightHeight) {
                    if (leftHeight >= grid[y][left]) {
                        gridHorizontal[y][left] = Int.MAX_VALUE
                    }
                    leftHeight = max(grid[y][left], leftHeight)
                    left++
                } else {
                    if (rightHeight >= grid[y][right]) {
                        gridHorizontal[y][right] = Int.MAX_VALUE
                    }
                    rightHeight = max(grid[y][right], rightHeight)
                    right--
                }
            }
        }

        val gridVertical = parseInput(input)
        for (x in 1..gridVertical[0].size - 2) {
            var top = 1
            var bottom = gridVertical.size - 2
            var topHeight = gridVertical[0][x]
            var bottomHeight = gridVertical[gridVertical.size - 1][x]

            for (y in 1..gridVertical.size - 2) {
                if (topHeight < bottomHeight) {
                    if (topHeight >= grid[top][x]) {
                        gridVertical[top][x] = Int.MAX_VALUE
                    }

                    topHeight = max(grid[top][x], topHeight)
                    top++
                } else {
                    if (bottomHeight >= grid[bottom][x]) {
                        gridVertical[bottom][x] = Int.MAX_VALUE
                    }
                    bottomHeight = max(grid[bottom][x], bottomHeight)
                    bottom--
                }
            }
        }

        for (y in grid.indices) {
            for (x in grid[y].indices) {
                if (grid[y][x] == gridHorizontal[y][x] || grid[y][x] == gridVertical[y][x]) {
                    counter++
                }
            }
        }
        return counter
    }

    fun part2(input: List<String>): Int {
        val grid = parseInput(input)
        val output = parseInput(input).map { it.map { 0 }.toMutableList() }.toMutableList()

        var viewDistance: Int
        val distances = ArrayList<List<Int>>()

        for (y in 1..grid.size - 2) {
            viewDistance = 0
            distances.clear()

            grid[y].windowed(2).withIndex().forEach { (x, items) ->
                if (x == grid[y].size - 2) {
                    return@forEach
                }
                val (prev, current) = items
                if (current > prev) {
                    for (j in distances.size - 1 downTo 0) {
                        val (stackItem, dist) = distances[j]
                        if (current > stackItem) {
                            viewDistance += dist
                            distances.removeLast()
                        } else {
                            break
                        }
                    }
                    viewDistance++
                } else {
                    distances.add(listOf(prev, viewDistance))
                    viewDistance = 1
                }
                output[y][x + 1] = viewDistance
            }

            viewDistance = 0
            distances.clear()
            grid[y].reversed().windowed(2).withIndex().forEach { (x, items) ->
                if (x == grid[y].size - 2) {
                    return@forEach
                }
                val (prev, current) = items
                if (current > prev) {
                    for (j in distances.size - 1 downTo 0) {
                        val (stackItem, dist) = distances[j]
                        if (current > stackItem) {
                            viewDistance += dist
                            distances.removeLast()
                        } else {
                            break
                        }
                    }
                    viewDistance++
                } else {
                    distances.add(listOf(prev, viewDistance))
                    viewDistance = 1
                }
                output[y][grid[y].size - 2 - x] *= viewDistance
            }
        }

        for (x in 1..grid[0].size - 2) {
            viewDistance = 0
            distances.clear()

            grid.map { it[x] }.windowed(2).withIndex().forEach { (y, items) ->
                if (y == grid.size - 2) {
                    return@forEach
                }
                val (prev, current) = items
                if (current > prev) {
                    for (j in distances.size - 1 downTo 0) {
                        val (stackItem, dist) = distances[j]
                        if (current > stackItem) {
                            viewDistance += dist
                            distances.removeLast()
                        } else {
                            break
                        }
                    }
                    viewDistance++
                } else {
                    distances.add(listOf(prev, viewDistance))
                    viewDistance = 1
                }
                output[y + 1][x] *= viewDistance
            }

            viewDistance = 0
            distances.clear()
            grid.reversed().map { it[x] }.windowed(2).withIndex().forEach { (y, items) ->
                if (y == grid.size - 2) {
                    return@forEach
                }
                val (prev, current) = items
                if (current > prev) {
                    for (j in distances.size - 1 downTo 0) {
                        val (stackItem, dist) = distances[j]
                        if (current > stackItem) {
                            viewDistance += dist
                            distances.removeLast()
                        } else {
                            break
                        }
                    }
                    viewDistance++
                } else {
                    distances.add(listOf(prev, viewDistance))
                    viewDistance = 1
                }
                output[grid.size - 2 - y][x] *= viewDistance
            }
        }

        return output.flatten().max()
    }

    val testInput = readInput("Day08_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
