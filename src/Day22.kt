import arrow.core.Either

private enum class Facing {
    RIGHT, DOWN, LEFT, UP
}

private enum class Turn {
    LEFT, RIGHT
}
fun main() {
    fun parseInput(input: List<List<String>>): Pair<Map<Pair<Int, Int>, Boolean>, List<Either<Int, Turn>>> {
        val map = input[0].mapIndexed { y, line ->
            line.withIndex().filter { x -> x.value != ' ' }.map { x ->
                Pair(y, x.index) to (x.value == '.')
            }
        }.flatten().toMap()

        val insString = input[1][0]
        val instructions: MutableList<Either<Int, Turn>> = mutableListOf()
        var leftPointer = 0
        for (i in insString.indices) {
            when (val c = insString[i]) {
                'L', 'R' -> {
                    if (leftPointer != i) {
                        instructions.add(Either.Left(insString.substring(leftPointer, i).toInt()))
                    }
                    instructions.add(Either.Right(if (c == 'L') Turn.LEFT else Turn.RIGHT))
                    leftPointer = i + 1
                }
                else -> {}
            }
        }
        if (leftPointer != insString.length) {
            instructions.add(Either.Left(insString.substring(leftPointer).toInt()))
        }
        return Pair(map, instructions)
    }

    fun part1(input: List<List<String>>): Int {
        val (map, instructions) = parseInput(input)
        var current = map.filter { it.key.first == 0 }.minBy { it.key.second }.key
        var facing = Facing.RIGHT
        for (ins in instructions.withIndex()) {
            when (val d = ins.value) {
                is Either.Left -> {
                    val (y, x) = current
                    val (dy, dx) = when (facing) {
                        Facing.RIGHT -> Pair(0, 1)
                        Facing.DOWN -> Pair(1, 0)
                        Facing.LEFT -> Pair(0, -1)
                        Facing.UP -> Pair(-1, 0)
                    }
                    for (i in 0 until d.value) {
                        var next = Pair(current.first + dy, current.second + dx)
                        if (next !in map) {
                            next = when (facing) {
                                Facing.RIGHT -> map.keys.filter { it.first == y }.minBy { it.second }
                                Facing.DOWN -> map.keys.filter { it.second == x }.minBy { it.first }
                                Facing.LEFT -> map.keys.filter { it.first == y }.maxBy { it.second }
                                Facing.UP -> map.keys.filter { it.second == x }.maxBy { it.first }
                            }
                        }

                        if (!map[next]!!) {
                            break
                        }
                        current = next
                    }
                }
                is Either.Right -> {
                    facing = when (facing) {
                        Facing.RIGHT -> if (d.value == Turn.LEFT) Facing.UP else Facing.DOWN
                        Facing.DOWN -> if (d.value == Turn.LEFT) Facing.RIGHT else Facing.LEFT
                        Facing.LEFT -> if (d.value == Turn.LEFT) Facing.DOWN else Facing.UP
                        Facing.UP -> if (d.value == Turn.LEFT) Facing.LEFT else Facing.RIGHT
                    }
                }
            }
        }

        return 1000 * (current.first + 1) + 4 * (current.second + 1) + facing.ordinal
    }

    val testInput = readInputDoubleNewline("Day22_test")
    check(part1(testInput) == 6032)
    // check(part2(testInput) == 5031)

    val input = readInputDoubleNewline("Day22")
    println(part1(input))
    // println(part2(input))
}
