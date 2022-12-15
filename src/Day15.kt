import kotlin.math.abs
import kotlin.math.max

fun main() {
    fun manhattanDistance(a: Pair<Long, Long>, b: Pair<Long, Long>): Long {
        return abs(a.first - b.first) + abs(a.second - b.second)
    }
    class Station(val pos: Pair<Long, Long>, val closestRadio: Pair<Long, Long>) {
        val distance = manhattanDistance(this.pos, this.closestRadio)
    }

    fun parseInput(input: List<String>): List<Station> {
        val regex = Regex("Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)")
        return input.map { line ->
            val (x0, y0, x1, y1) = regex.matchEntire(line)!!.destructured
            Station(Pair(x0.toLong(), y0.toLong()), Pair(x1.toLong(), y1.toLong()))
        }.sortedBy { it.pos.first }
    }

    fun coveredInLine(stations: List<Station>, y: Long): List<LongProgression> {
        var covered = mutableListOf<LongProgression>()
        stations
            .forEach { station ->
                val closestDistanceToLine = manhattanDistance(station.pos, Pair(station.pos.first, y))
                val distanceToClosestRadio = station.distance
                if (closestDistanceToLine > distanceToClosestRadio) {
                    return@forEach
                }
                val radius = abs(closestDistanceToLine - station.distance)
                covered.add(station.pos.first - radius..station.pos.first + radius)
            }
        covered = covered.sortedBy { it.first }.toMutableList()
        var i = 0
        while (i < covered.size - 1) {
            if (covered[i].last >= covered[i + 1].first) {
                covered[i] = covered[i].first..max(covered[i].last, covered[i + 1].last)
                covered.removeAt(i + 1)
            } else {
                i++
            }
        }
        return covered.toList()
    }

    fun part1(input: List<String>, targetY: Long): Long {
        val stations = parseInput(input)
        val covered = coveredInLine(stations, targetY)

        val containsRadioCnt = stations
            .map { it.closestRadio.second }
            .distinct()
            .filter { it == targetY }
            .size

        return covered.sumOf { it.count().toLong() } - containsRadioCnt
    }

    fun part2(input: List<String>, size: Long): Long {
        val stations = parseInput(input)

        val blocked = stations.map(Station::closestRadio).toSet()

        for (y in 0..size) {
            val covered = coveredInLine(stations, y)
                .filter { it.last >= 0 && it.first <= size }

            for ((a, b) in covered.windowed(2)) {
                for (x in a.last + 1 until b.first) {
                    if (Pair(x, y) !in blocked) {
                        return x * 4000000 + y
                    }
                }
            }
        }

        throw Exception("No solution found")
    }

    val testInput = readInput("Day15_test")
    check(part1(testInput, 10) == 26L)
    check(part2(testInput, 20) == 56000011L)

    val input = readInput("Day15")
    println(part1(input, 2000000))
    println(part2(input, 4000000))
}
