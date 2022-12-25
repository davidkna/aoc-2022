import arrow.core.Either

private enum class Facing {
    Right, Down, Left, Up
}

private enum class Turn {
    Left, Right
}

fun main() {
    fun parseInput(input: List<List<String>>): Pair<Map<Pair<Int, Int>, Boolean>, List<Either<Int, Turn>>> {
        val map = input[0].mapIndexed { y, line ->
            line.withIndex().filter { x -> x.value != ' ' }.map { x ->
                Pair(y + 1, x.index + 1) to (x.value == '.')
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
                    instructions.add(Either.Right(if (c == 'L') Turn.Left else Turn.Right))
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
        var current = map.filter { it.key.first == 1 }.minBy { it.key.second }.key
        var facing = Facing.Right
        for (ins in instructions.withIndex()) {
            when (val d = ins.value) {
                is Either.Left -> {
                    val (y, x) = current
                    val (dy, dx) = when (facing) {
                        Facing.Right -> Pair(0, 1)
                        Facing.Down -> Pair(1, 0)
                        Facing.Left -> Pair(0, -1)
                        Facing.Up -> Pair(-1, 0)
                    }
                    for (i in 0 until d.value) {
                        var next = Pair(current.first + dy, current.second + dx)
                        if (next !in map) {
                            next = when (facing) {
                                Facing.Right -> map.keys.filter { it.first == y }.minBy { it.second }
                                Facing.Down -> map.keys.filter { it.second == x }.minBy { it.first }
                                Facing.Left -> map.keys.filter { it.first == y }.maxBy { it.second }
                                Facing.Up -> map.keys.filter { it.second == x }.maxBy { it.first }
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
                        Facing.Right -> if (d.value == Turn.Left) Facing.Up else Facing.Down
                        Facing.Down -> if (d.value == Turn.Left) Facing.Right else Facing.Left
                        Facing.Left -> if (d.value == Turn.Left) Facing.Down else Facing.Up
                        Facing.Up -> if (d.value == Turn.Left) Facing.Left else Facing.Right
                    }
                }
            }
        }

        return 1000 * current.first + 4 * current.second + facing.ordinal
    }

    fun wrapPart2(pos: Pair<Int, Int>, facing: Facing): Pair<Pair<Int, Int>, Facing> {
        val (y, x) = pos
        return if (y in 1..50 && x in 51..100) {
            when (facing) {
                Facing.Left -> {
                    ((151 - y to 1) to Facing.Right)
                }

                Facing.Up -> {
                    ((x + 100 to 1) to Facing.Right)
                }

                else -> {
                    throw RuntimeException("pos=($y, $x), facing=$facing")
                }
            }
        } else if (y in 1..50 && x in 101..150) {
            when (facing) {
                Facing.Right -> {
                    ((151 - y to 100) to Facing.Left)
                }

                Facing.Down -> {
                    ((x - 50 to 100) to Facing.Left)
                }

                Facing.Up -> {
                    ((200 to x - 100) to Facing.Up)
                }

                else -> {
                    throw RuntimeException("pos=($y, $x), facing=$facing")
                }
            }
        } else if (y in 51..100 && x in 51..100) {
            when (facing) {
                Facing.Right -> {
                    ((50 to y + 50) to Facing.Up)
                }

                Facing.Left -> {
                    ((101 to y - 50) to Facing.Down)
                }

                else -> {
                    throw RuntimeException("pos=($y, $x), facing=$facing")
                }
            }
        } else if (y in 101..150 && x in 51..100) {
            when (facing) {
                Facing.Right -> {
                    ((151 - y to 150) to Facing.Left)
                }

                Facing.Down -> {
                    ((x + 100 to 50) to Facing.Left)
                }

                else -> {
                    throw RuntimeException("pos=($y, $x), facing=$facing")
                }
            }
        } else if (y in 101..150 && x in 1..50) {
            return when (facing) {
                Facing.Left -> {
                    ((151 - y to 51) to Facing.Right)
                }

                Facing.Up -> {
                    ((x + 50 to 51) to Facing.Right)
                }

                else -> {
                    throw RuntimeException("pos=($y, $x), facing=$facing")
                }
            }
        } else if (y in 151..200 && x in 1..50) {
            when (facing) {
                Facing.Right -> {
                    return ((150 to y - 100) to Facing.Up)
                }

                Facing.Down -> {
                    return ((1 to x + 100) to Facing.Down)
                }

                Facing.Left -> {
                    return ((1 to y - 100) to Facing.Down)
                }

                else -> {
                    throw RuntimeException("pos=($y, $x), facing=$facing")
                }
            }
        } else {
            throw RuntimeException("pos=($y, $x), facing=$facing")
        }
    }

    fun part2(input: List<List<String>>): Int {
        val (map, instructions) = parseInput(input)
        var current = map.filter { it.key.first == 1 }.minBy { it.key.second }.key
        var facing = Facing.Right
        for (ins in instructions.withIndex()) {
            when (val d = ins.value) {
                is Either.Left -> {
                    for (i in 0 until d.value) {
                        val (y, x) = current
                        val (dy, dx) = when (facing) {
                            Facing.Right -> Pair(0, 1)
                            Facing.Down -> Pair(1, 0)
                            Facing.Left -> Pair(0, -1)
                            Facing.Up -> Pair(-1, 0)
                        }
                        var next = Pair(y + dy, x + dx)
                        var nextFacing = facing
                        if (next !in map) {
                            val (newPos, newF) = wrapPart2(current, facing)
                            next = newPos
                            nextFacing = newF
                        }

                        if (!map[next]!!) {
                            break
                        }
                        facing = nextFacing
                        current = next
                    }
                }

                is Either.Right -> {
                    facing = when (facing) {
                        Facing.Right -> if (d.value == Turn.Left) Facing.Up else Facing.Down
                        Facing.Down -> if (d.value == Turn.Left) Facing.Right else Facing.Left
                        Facing.Left -> if (d.value == Turn.Left) Facing.Down else Facing.Up
                        Facing.Up -> if (d.value == Turn.Left) Facing.Left else Facing.Right
                    }
                }
            }
        }

        return 1000 * current.first + 4 * current.second + facing.ordinal
    }

    val testInput = readInputDoubleNewline("Day22_test")
    check(part1(testInput) == 6032)

    val input = readInputDoubleNewline("Day22")
    println(part1(input))
    println(part2(input))
}
