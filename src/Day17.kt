import java.util.BitSet

private enum class WindDirection {
    LEFT, RIGHT
}

private enum class TetrisBlock {
    ROW, PLUS, REVERSE_L, COLUMN, SQUARE;

    fun toPointCloud(x: Int, y: Long): List<Pair<Int, Long>> {
        return when (this) {
            ROW -> listOf(
                x to y,
                x + 1 to y,
                x + 2 to y,
                x + 3 to y,
            )
            PLUS -> listOf(
                x + 1 to y,
                x to y + 1,
                x + 1 to y + 1,
                x + 2 to y + 1,
                x + 1 to y + 2,
            )
            REVERSE_L -> listOf(
                x to y,
                x + 1 to y,
                x + 2 to y,
                x + 2 to y + 1,
                x + 2 to y + 2,
            )
            COLUMN -> listOf(
                x to y,
                x to y + 1,
                x to y + 2,
                x to y + 3,
            )
            SQUARE -> listOf(
                x to y,
                x + 1 to y,
                x to y + 1,
                x + 1 to y + 1,
            )
        }
    }
}

fun main() {
    fun parseInput(input: String) = input.map {
        when (it) {
            '<' -> WindDirection.LEFT
            '>' -> WindDirection.RIGHT
            else -> throw IllegalArgumentException("Unknown direction $it")
        }
    }.toList()

    fun solution(input: String, rounds: Long): Long {
        val windPattern = parseInput(input)
        val blockPattern = listOf(
            TetrisBlock.ROW,
            TetrisBlock.PLUS,
            TetrisBlock.REVERSE_L,
            TetrisBlock.COLUMN,
            TetrisBlock.SQUARE,
        )

        var highestBlock = -1L
        var finalScoreOffset = 0L
        val chambWidth = 7
        val map = HashSet<Pair<Int, Long>>()
        val repetitonMemo = HashMap<Triple<List<BitSet>, Int, Int>, Pair<Long, Long>>()
        var foundCycle = false

        var windIdx = 0
        var blockIdx = 0
        val floor = 0L

        var round = 0L
        while (round++ < rounds) {
            val initialPosition = Pair(2, highestBlock + 4)
            val block = blockPattern[blockIdx]

            var blockPoints = block.toPointCloud(initialPosition.first, initialPosition.second)

            if (round > 300 && !foundCycle) {
                val image = (0L..100L).map { highestBlock - it }.map { y ->
                    (0..chambWidth).fold(BitSet()) { acc, x ->
                        if (map.contains(Pair(x, y))) {
                            acc.set(x + 1)
                        }
                        acc
                    }
                }.toList()
                val memoKey = Triple(image, windIdx, blockIdx)
                if (repetitonMemo.containsKey(memoKey)) {
                    foundCycle = true

                    val (skipFrom, highestBlockAt) = repetitonMemo[memoKey]!!
                    repetitonMemo.clear()
                    val cycleSize = round - skipFrom
                    val cyclesToSkip = (rounds - skipFrom).floorDiv(cycleSize) - 1
                    round += cyclesToSkip * cycleSize
                    finalScoreOffset = cyclesToSkip * (highestBlock - highestBlockAt)
                } else {
                    repetitonMemo[memoKey] = Pair(round, highestBlock)
                }
            }

            while (true) {
                val wind = windPattern[windIdx]

                val blockPointsAfterWind = blockPoints.map { (x, y) ->
                    when (wind) {
                        WindDirection.LEFT -> x - 1 to y
                        WindDirection.RIGHT -> x + 1 to y
                    }
                }
                val isLegal = blockPointsAfterWind.all { (x, y) ->
                    x in 0 until chambWidth && y >= floor && (x to y) !in map
                }

                windIdx = (windIdx + 1) % windPattern.size

                if (isLegal) {
                    blockPoints = blockPointsAfterWind
                }

                // Move block down
                val blockPointsAfterGravity = blockPoints.map { (x, y) -> x to y - 1 }
                val isLegalAfterGravity = blockPointsAfterGravity.all { (x, y) ->
                    y >= floor && (x to y) !in map
                }

                if (isLegalAfterGravity) {
                    blockPoints = blockPointsAfterGravity
                } else {
                    val highestPointInBlock = blockPoints.maxByOrNull { (_, y) -> y }!!.second
                    highestBlock = maxOf(highestBlock, highestPointInBlock)

                    map.addAll(blockPoints)
                    break
                }
            }

            blockIdx = (blockIdx + 1) % blockPattern.size
        }

        return highestBlock + 1 + finalScoreOffset
    }

    fun part1(input: String) = solution(input, 2022)

    fun part2(input: String) = solution(input, 1000000000000)

    val testInput = readInput("Day17_test")[0]
    check(part1(testInput) == 3068L)
    check(part2(testInput) == 1514285714288)

    val input = readInput("Day17")[0]
    println(part1(input))
    println(part2(input))
}
