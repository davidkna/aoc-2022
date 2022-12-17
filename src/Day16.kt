import java.util.BitSet

fun main() {
    class Valve(val rate: Int, val targets: List<Int>)

    fun parseInput(input: List<String>): List<Valve> {
        val regex = Regex("Valve (\\w+) has flow rate=(\\d+); tunnels? leads? to valves? (.+)")
        val tempMap: Map<String, Pair<Int, List<String>>> = input.associate { line ->
            val (name, rate, targets) = regex.matchEntire(line)!!.destructured
            name to Pair(rate.toInt(), targets.split(", "))
        }.toMap()

        val idxToName = tempMap.keys.sortedBy { it }.withIndex().associate { it.index to it.value }.toMap()
        val nameToIdx = idxToName.map { it.value to it.key }.toMap()

        return (0 until tempMap.size).map { idx ->
            val v = tempMap[idxToName[idx]!!]!!
            Valve(v.first, v.second.map { nameToIdx[it]!! })
        }.toList()
    }

    fun findShortestPaths(valves: List<Valve>): Map<Int, Map<Int, Int>> {
        val shortestPaths = mutableMapOf<Int, Map<Int, Int>>()
        val allNodes = valves.indices
        allNodes.forEach { node ->
            shortestPaths[node] = allNodes.associateWith {
                if (node == it) 0 else if (valves[node].targets.contains(it)) 1 else 99999
            }
        }

        allNodes.forEach { node ->
            allNodes.forEach { source ->
                allNodes.forEach { target ->
                    val newDistance = shortestPaths[source]!![node]!! + shortestPaths[node]!![target]!!
                    if (newDistance < shortestPaths[source]!![target]!!) {
                        shortestPaths[source] = shortestPaths[source]!!.toMutableMap().apply {
                            this[target] = newDistance
                        }
                    }
                }
            }
        }

        return shortestPaths
            .map { (k, v) ->
                k to v
                    .filter { valves[it.key].rate > 0 && it.key != k }
            }.toMap()
    }

    fun part1(input: List<String>): Int {
        val valves = parseInput(input)

        val cache = mutableMapOf<Triple<Int, Int, BitSet>, Int>()
        val shortestPaths = findShortestPaths(valves)
        fun makeBestChoice(current: Int, remainingSteps: Int, opened: BitSet): Int {
            if (Triple(current, remainingSteps, opened) in cache) {
                return cache[Triple(current, remainingSteps, opened)]!!
            }
            val result = shortestPaths[current]!!
                .filter { !opened.get(it.key + 1) && (remainingSteps - (it.value + 1)) >= 0 }
                .map { (target, distance) ->
                    val stepsLeftAfterOpen = remainingSteps - (distance + 1)
                    val newOpened = (opened.clone() as BitSet).apply { set(target + 1) }
                    valves[target].rate * stepsLeftAfterOpen + makeBestChoice(target, stepsLeftAfterOpen, newOpened)
                }.maxOrNull() ?: 0
            cache[Triple(current, remainingSteps, opened)] = result
            return result
        }

        return makeBestChoice(0, 30, BitSet())
    }

    fun part2(input: List<String>): Int {
        val valves = parseInput(input)

        val shortestPaths = findShortestPaths(valves)
        val cache = mutableMapOf<Triple<Pair<Int, Int>, Pair<Int, Int>, BitSet>, Int>()
        fun makeBestChoice(current: Pair<Int, Int>, remainingSteps: Pair<Int, Int>, opened: BitSet): Int {
            if (remainingSteps.first < 0 && remainingSteps.second < 0) {
                return 0
            }
            if (remainingSteps.first < remainingSteps.second) {
                return makeBestChoice(current.swap(), remainingSteps.swap(), opened)
            }

            if (Triple(current, remainingSteps, opened) in cache) {
                return cache[Triple(current, remainingSteps, opened)]!!
            }
            val result = listOf(0, 1)
                .filter { remainingSteps.get(it) >= 0 }
                .flatMap { player ->
                    val playerCurrent = current.get(player)
                    val playerRemainingSteps = remainingSteps.get(player)

                    shortestPaths[playerCurrent]!!
                        .filter { !opened.get(it.key + 1) && (playerRemainingSteps - (it.value - 1)) >= 0 }
                        .map { (target, distance) ->
                            val stepsLeftAfterOpen = playerRemainingSteps - (distance + 1)
                            val newOpened = (opened.clone() as BitSet).apply { set(target + 1) }
                            val newRemainingSteps = remainingSteps.set(player, stepsLeftAfterOpen)
                            val newCurrent = current.set(player, target)
                            valves[target].rate * stepsLeftAfterOpen + makeBestChoice(newCurrent, newRemainingSteps, newOpened)
                        }
                }.maxOrNull() ?: 0

            cache[Triple(current, remainingSteps, opened)] = result
            return result
        }
        return makeBestChoice(Pair(0, 0), Pair(26, 26), BitSet())
    }

    val testInput = readInput("Day16_test")
    check(part1(testInput) == 1651)
    check(part2(testInput) == 1707)

    val input = readInput("Day16")
    println(part1(input))
    println(part2(input))
}

private fun <A> Pair<A, A>.get(i: Int): A {
    return when (i) {
        0 -> first
        1 -> second
        else -> throw IllegalArgumentException()
    }
}
private fun <A> Pair<A, A>.set(i: Int, value: A): Pair<A, A> {
    return when (i) {
        0 -> Pair(value, second)
        1 -> Pair(first, value)
        else -> throw IllegalArgumentException()
    }
}

private fun <A> Pair<A, A>.swap(): Pair<A, A> {
    return Pair(second, first)
}
