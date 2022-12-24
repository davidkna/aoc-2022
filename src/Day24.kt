import java.util.BitSet
import java.util.PriorityQueue
import kotlin.math.abs

private enum class WinDir { UP, DOWN, LEFT, RIGHT }

fun main() {
    class StormMap(val map: Map<Pair<Int, Int>, BitSet>, val dimensions: Pair<Int, Int>) {
        val start = Pair(0, 1)
        val end = Pair(dimensions.first - 1, dimensions.second - 2)

        fun step(): StormMap {
            val newStormMap = mutableMapOf<Pair<Int, Int>, BitSet>()
            map.forEach { (pos, bitSet) ->
                val (y, x) = pos
                WinDir.values().filter { dir -> bitSet.get(dir.ordinal + 1) }.forEach { dir ->

                    val (dy, dx) = when (dir) {
                        WinDir.UP -> Pair(-1, 0)
                        WinDir.DOWN -> Pair(1, 0)
                        WinDir.LEFT -> Pair(0, -1)
                        WinDir.RIGHT -> Pair(0, 1)
                    }
                    var next = Pair(y + dy, x + dx)
                    if (next.first <= 0 || next.second <= 0 || next.first >= dimensions.first - 1 || next.second >= dimensions.second - 1) {
                        next = when (dir) {
                            WinDir.DOWN -> Pair(1, x)
                            WinDir.UP -> Pair(dimensions.first - 2, x)
                            WinDir.LEFT -> Pair(y, dimensions.second - 2)
                            WinDir.RIGHT -> Pair(y, 1)
                        }
                    }
                    if (next !in newStormMap) {
                        newStormMap[next] = BitSet().apply { set(dir.ordinal + 1) }
                    } else {
                        newStormMap[next]!!.set(dir.ordinal + 1)
                    }
                }
            }
            return StormMap(newStormMap.toMap(), dimensions)
        }

        fun destinations(y: Int, x: Int): List<Pair<Int, Int>> {
            return listOf(
                Pair(y - 1, x),
                Pair(y + 1, x),
                Pair(y, x - 1),
                Pair(y, x + 1),
                Pair(y, x),
            ).filter { it == start || it == end || it.first > 0 && it.first < dimensions.first - 1 && it.second > 0 && it.second < dimensions.second - 1 }
                .filter { it !in map }
        }
    }

    fun parseInput(input: List<String>): StormMap {
        val storms = input.mapIndexed { y, line ->
            line.withIndex().filter { x -> listOf('>', '<', '^', 'v').contains(x.value) }.map { x ->
                val winDir = when (x.value) {
                    '>' -> WinDir.RIGHT
                    '<' -> WinDir.LEFT
                    '^' -> WinDir.UP
                    'v' -> WinDir.DOWN
                    else -> throw IllegalArgumentException()
                }
                Pair(y, x.index) to BitSet().apply { set(winDir.ordinal + 1) }
            }
        }.flatten().toMap()
        return StormMap(storms, Pair(input.size, input[0].length))
    }

    fun part1(input: List<String>): Int {
        val stormMap = parseInput(input)
        val stormAtTime = mutableListOf(stormMap)

        fun heuristic(pos: Pair<Int, Int>): Int {
            return abs(pos.first - stormMap.end.first) + abs(pos.second - stormMap.end.second)
        }

        val queue: PriorityQueue<Pair<Int, Pair<Int, Int>>> = PriorityQueue(
            compareBy {
                it.first + heuristic(it.second)
            },
        )
        queue.add(Pair(0, stormMap.start))
        val visited = mutableSetOf(Pair(0, stormMap.start))
        while (queue.isNotEmpty()) {
            val (cost, pos) = queue.poll()
            if (pos == stormMap.end) {
                return cost - 1
            }
            if (stormAtTime.indices.last < cost) {
                stormAtTime.add(stormAtTime.last().step())
            }
            stormAtTime[cost].destinations(pos.first, pos.second)
                .map { dest -> Pair(cost + 1, dest) }
                .filter { visited.add(it) }
                .forEach { queue.add(it) }
        }

        // no solution
        throw IllegalArgumentException()
    }

    fun part2(input: List<String>): Int {
        val stormMap = parseInput(input)
        val stormAtTime = mutableListOf(stormMap)
        val minRoundTrip =
            abs(stormMap.start.first - stormMap.end.first) + abs(stormMap.start.second - stormMap.end.second)

        fun heuristic(targetAndPos: Pair<Int, Pair<Int, Int>>): Int {
            val (targetId, pos) = targetAndPos
            return when (targetId) {
                0 -> 2 * minRoundTrip + abs(pos.first - stormMap.end.first) + abs(pos.second - stormMap.end.second)
                1 -> minRoundTrip + abs(pos.first - stormMap.start.first) + abs(pos.second - stormMap.start.second)
                2 -> abs(pos.first - stormMap.end.first) + abs(pos.second - stormMap.end.second)
                else -> throw IllegalArgumentException()
            }
        }

        val queue: PriorityQueue<Triple<Int, Int, Pair<Int, Int>>> = PriorityQueue(
            compareBy {
                it.first + heuristic(Pair(it.second, it.third))
            },
        )
        queue.add(Triple(0, 0, stormMap.start))
        val visited = mutableSetOf(Triple(0, 0, stormMap.start))
        while (queue.isNotEmpty()) {
            val (cost, targetId, pos) = queue.poll()
            var nextTargetId = targetId
            if (targetId == 0 && pos == stormMap.end) {
                nextTargetId = 1
            } else if (targetId == 1 && pos == stormMap.start) {
                nextTargetId = 2
            } else if (targetId == 2 && pos == stormMap.end) {
                return cost - 1
            }
            if (stormAtTime.indices.last < cost) {
                stormAtTime.add(stormAtTime.last().step())
            }
            stormAtTime[cost].destinations(pos.first, pos.second)
                .map { dest -> Triple(cost + 1, nextTargetId, dest) }
                .filter { visited.add(it) }
                .forEach { queue.add(it) }
        }

        // no solution
        throw IllegalArgumentException()
    }

    val testInput = readInput("Day24_test")
    check(part1(testInput) == 18)
    check(part2(testInput) == 54)

    val input = readInput("Day24")
    println(part1(input))
    println(part2(input))
}
