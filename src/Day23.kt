fun main() {
    fun parseInput(input: List<String>): Set<Pair<Int, Int>> {
        return input.mapIndexed { y, line ->
            line.withIndex().filter { x -> x.value == '#' }.map { x ->
                Pair(y, x.index)
            }
        }.flatten().toSet()
    }

    fun neighbors(p: Pair<Int, Int>): List<Pair<Int, Int>> {
        val (y, x) = p
        return listOf(
            Pair(y - 1, x - 1),
            Pair(y - 1, x),
            Pair(y - 1, x + 1),
            Pair(y, x - 1),
            Pair(y, x + 1),
            Pair(y + 1, x - 1),
            Pair(y + 1, x),
            Pair(y + 1, x + 1),
        )
    }

    fun addToNewPositions(
        p: Pair<Int, Int>,
        target: Pair<Int, Int>,
        newPositions: MutableMap<Pair<Int, Int>, MutableList<Pair<Int, Int>>>
    ) {
        if (target !in newPositions) {
            newPositions[target] = mutableListOf(p)
            return
        }
        newPositions[target]!!.add(p)
    }

    fun solution(input: List<String>, cutoff: Int, part2: Boolean): Int {
        var elves = parseInput(input)

        for (round in 0..cutoff) {
            val newPositions: MutableMap<Pair<Int, Int>, MutableList<Pair<Int, Int>>> = mutableMapOf()

            elves.forEach { elf ->
                val (y, x) = elf
                val n = neighbors(elf).filter { it in elves }
                if (n.isEmpty()) {
                    addToNewPositions(elf, elf, newPositions)
                    return@forEach
                }

                for (i in 0 until 4) {
                    when ((i + round) % 4) {
                        0 -> {
                            if (listOf(Pair(y - 1, x - 1), Pair(y - 1, x), Pair(y - 1, x + 1)).all { it !in n }) {
                                addToNewPositions(elf, Pair(y - 1, x), newPositions)
                                return@forEach
                            }
                        }

                        1 -> {
                            if (listOf(Pair(y + 1, x - 1), Pair(y + 1, x), Pair(y + 1, x + 1)).all { it !in n }) {
                                addToNewPositions(elf, Pair(y + 1, x), newPositions)
                                return@forEach
                            }
                        }

                        2 -> {
                            if (listOf(Pair(y + 1, x - 1), Pair(y, x - 1), Pair(y - 1, x - 1)).all { it !in n }) {
                                addToNewPositions(elf, Pair(y, x - 1), newPositions)
                                return@forEach
                            }
                        }

                        3 -> {
                            if (listOf(Pair(y + 1, x + 1), Pair(y, x + 1), Pair(y - 1, x + 1)).all { it !in n }) {
                                addToNewPositions(elf, Pair(y, x + 1), newPositions)
                                return@forEach
                            }
                        }
                    }
                }

                addToNewPositions(elf, elf, newPositions)
            }

            var moved = false
            elves = newPositions.flatMap { (target, positions) ->
                if (positions.size == 1) {
                    moved = moved || positions[0] != target
                    listOf(target)
                } else {
                    positions
                }
            }.toSet()

            if (!moved) {
                if (part2) {
                    return round + 1
                }
                break
            }
        }

        return (1 + elves.maxBy { it.first }.first - elves.minBy { it.first }.first) * (1 + elves.maxBy { it.second }.second - elves.minBy { it.second }.second) - elves.size
    }

    fun part1(input: List<String>) = solution(input, 10, false)
    fun part2(input: List<String>) = solution(input, Int.MAX_VALUE, true)

    val testInput = readInput("Day23_test")
    check(part1(testInput) == 110)
    check(part2(testInput) == 20)

    val input = readInput("Day23")
    println(part1(input))
    println(part2(input))
}
