
enum class MonkeyOp { ADD, MUL, SQUARE }

fun main() {
    class Monkey(
        var items: ArrayDeque<Long>,
        var op: Pair<MonkeyOp, Long>,
        var test: Long,
        var decision: Pair<Long, Long>,
    ) {
        var count = 0

        fun add(x: Long) {
            items.addLast(x)
        }

        fun take(): Long {
            count++
            return items.removeFirst()
        }
    }

    fun solution(input: List<List<String>>, relief: Boolean, rounds: Long): Long {
        val monkeys = input.map {
                description ->
            val items = description[1].substring(18).split(", ").map { it.toLong() }
            val op = when (description[2][23]) {
                '+' -> Pair(MonkeyOp.ADD, description[2].substring(25).toLong())
                '*' -> {
                    if (description[2].substring(25) == "old") {
                        Pair(MonkeyOp.SQUARE, 1L)
                    } else {
                        Pair(MonkeyOp.MUL, description[2].substring(25).toLong())
                    }
                }
                else -> throw Exception("Unknown op")
            }
            val test = description[3].substring(21).toLong()
            val decision = Pair(description[4].substring(29).toLong(), description[5].substring(30).toLong())
            Monkey(ArrayDeque(items), op, test, decision)
        }.toMutableList()

        val modBy = monkeys.map(Monkey::test).reduce { acc, i -> acc * i }

        for (round in 1..rounds) {
            for (monkey in monkeys) {
                for (j in 1..monkey.items.size) {
                    var item = monkey.take()

                    when (monkey.op.first) {
                        MonkeyOp.ADD -> item += monkey.op.second
                        MonkeyOp.MUL -> item *= monkey.op.second
                        MonkeyOp.SQUARE -> item *= item
                    }
                    if (relief) {
                        item /= 3.toLong()
                    }
                    item %= modBy
                    if (item % monkey.test == 0L) {
                        monkeys[monkey.decision.first.toInt()].add(item)
                    } else {
                        monkeys[monkey.decision.second.toInt()].add(item)
                    }
                }
            }
            if (listOf(1, 20, 1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000).contains(round.toInt())) {
                println("Round $round")
                for (monkey in monkeys) {
                    println("  Monkey ${monkeys.indexOf(monkey)}: ${monkey.count}")
                }
            }
        }

        monkeys.sortBy(Monkey::count)

        return monkeys.last().count.toLong() * monkeys[monkeys.size - 2].count.toLong()
    }

    val testInput = readInputDoubleNewline("Day11_test")
    check(solution(testInput, true, 20) == 10605L)
    check(solution(testInput, false, 10000) == 2713310158)

    val input = readInputDoubleNewline("Day11")
    println(solution(input, true, 20))
    println(solution(input, false, 10000))
}
