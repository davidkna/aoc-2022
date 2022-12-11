import java.math.BigInteger

enum class Op { ADD, MUL, SQUARE }

fun main() {
    class Monkey(
        var items: ArrayDeque<BigInteger>,
        var op: Pair<Op, Int>,
        var test: Int,
        var decision: Pair<Int, Int>
    ) {
        var count = 0

        fun add(x: BigInteger) {
            items.addLast(x)
        }

        fun take(): BigInteger {
            count++
            return items.removeFirst()
        }
    }

    fun solution(input: List<List<String>>, relief: Boolean, rounds: Int): Long {
        val monkeys = input.map {
                description ->
            val items = description[1].substring(18).split(", ").map { it.toBigInteger() }
            val op = when (description[2][23]) {
                '+' -> Pair(Op.ADD, description[2].substring(25).toInt())
                '*' -> {
                    if (description[2].substring(25) == "old") {
                        Pair(Op.SQUARE, 1)
                    } else {
                        Pair(Op.MUL, description[2].substring(25).toInt())
                    }
                }
                else -> throw Exception("Unknown op")
            }
            val test = description[3].substring(21).toInt()
            val decision = Pair(description[4].substring(29).toInt(), description[5].substring(30).toInt())
            Monkey(ArrayDeque(items), op, test, decision)
        }.toMutableList()

        //val modBy = monkeys.map(Monkey::test).reduce { acc, i -> acc * i }

        for (round in 1..rounds) {
            for (monkey in monkeys) {
                for (j in 1..monkey.items.size) {
                    var item = monkey.take()

                    when (monkey.op.first) {
                        Op.ADD -> item += monkey.op.second.toBigInteger()
                        Op.MUL -> item *= monkey.op.second.toBigInteger()
                        Op.SQUARE -> item *= item
                    }
                    if (relief) {
                        item /= 3.toBigInteger()
                    }
                    //item %= modBy
                    if (item % monkey.test.toBigInteger() == 0.toBigInteger()) {
                        monkeys[monkey.decision.first].add(item)
                    } else {
                        monkeys[monkey.decision.second].add(item)
                    }
                }
            }
            if (listOf(1, 20, 1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000).contains(round)) {
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
    //println(solution(testInput, false, 10000))
    check(solution(testInput, false, 10000) == 2713310158)

    val input = readInputDoubleNewline("Day11")
    println(solution(input, true, 20))
    println(solution(input, false, 10000))
}
