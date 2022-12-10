fun main() {
    fun part1(input: List<String>): Int {
        var x = 1
        var signalStrength = 0
        var cycle = -19

        input.map { it.split(" ") }.forEach { ins ->
            val cycleDuration = when (ins[0]) {
                "noop" -> 1
                "addx" -> 2
                else -> throw Exception("Unknown instruction")
            }
            for (i in 1..cycleDuration) {
                if (cycle % 40 == 0) {
                    signalStrength += x * (cycle + 20)
                }
                cycle++
            }
            x += ins.getOrElse(1) { "0" }.toInt()
        }

        return signalStrength
    }

    fun part2(input: List<String>): String {
        val output = arrayListOf(Array(40) { '.' })
        var x = 1
        var cycle = 0

        input.map { it.split(" ") }.forEach { ins ->
            val cycleDuration = when (ins[0]) {
                "noop" -> 1
                "addx" -> 2
                else -> throw Exception("Unknown instruction")
            }
            for (i in 1..cycleDuration) {
                if ((x - 1..x + 1).contains(cycle % 40)) {
                    if (cycle / 40 >= output.size) {
                        output.add(Array(40) { '.' })
                    }
                    output[cycle / 40][cycle % 40] = '#'
                }
                cycle++
            }
            x += ins.getOrElse(1) { "0" }.toInt()
        }

        return output.joinToString("\n") { it.joinToString("") }
    }

    val testInput = readInput("Day10_test")
    check(part1(testInput) == 13140)
    check(
        part2(testInput) == """
        ##..##..##..##..##..##..##..##..##..##..
        ###...###...###...###...###...###...###.
        ####....####....####....####....####....
        #####.....#####.....#####.....#####.....
        ######......######......######......####
        #######.......#######.......#######.....
        """.trimIndent()
    )
    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}
