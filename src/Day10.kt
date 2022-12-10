fun main() {
    fun part1(input: List<String>): Int {
        val ins = input.asReversed().toMutableList()
        for (i in 0 until 4) {
            ins.add("noop")
        }

        var x = 1
        var signalStrength = 0
        var cycle = 0

        while (ins.isNotEmpty()) {
            val s = ins.removeLast().split(" ")
            var cycleDuration = 0
            when (s[0]) {
                "noop" -> cycleDuration = 1
                "addx" -> cycleDuration = 2
            }
            for (i in 1..cycleDuration) {
                if (s[0] == "addx" && i == cycleDuration) {
                    x += s[1].toInt()
                }
                if ((cycle + 18) % 40 == 0) {
                    signalStrength += x * (cycle - 2)
                }
                cycle++
            }
        }

        return signalStrength
    }

    fun part2(input: List<String>): String {
        val ins = input.asReversed().toMutableList()
        for (i in 0 until 4) {
            ins.add("noop")
        }

        val output = arrayListOf(Array(40) { '.' })

        var x = 1
        var cycle = -2

        while (ins.isNotEmpty()) {
            val s = ins.removeLast().split(" ")
            var cycleDuration = 0
            when (s[0]) {
                "noop" -> cycleDuration = 1
                "addx" -> cycleDuration = 2
            }
            for (i in 1..cycleDuration) {
                if (s[0] == "addx" && i == cycleDuration) {
                    x += s[1].toInt()
                }
                if ((x - 1..x + 1).contains((cycle - 1) % 40)) {
                    if (cycle / 40 >= output.size) {
                        output.add(Array(40) { '.' })
                    }
                    output[cycle / 40][cycle % 40] = '#'
                }
                cycle++
            }
        }

        return output.joinToString("\n") { it.joinToString("") }
    }

    val testInput = readInput("Day10_test")
    check(part1(testInput) == 13140)
    println(part2(testInput))

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}
