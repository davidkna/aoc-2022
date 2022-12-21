import arrow.core.Either

private enum class MathOp { ADD, MUL, SUB, DIV;
    fun calc(x: Long, y: Long): Long {
        return when (this) {
            ADD -> x + y
            MUL -> x * y
            SUB -> x - y
            DIV -> x / y
        }
    }
}
fun main() {
    class Monkey(val monkeyA: String, val monkeyB: String, val op: MathOp)

    fun parseMonkey(input: String): Monkey {
        val monkeyA = input.substring(0, 4)
        val op = when (input[5]) {
            '+' -> MathOp.ADD
            '*' -> MathOp.MUL
            '-' -> MathOp.SUB
            '/' -> MathOp.DIV
            else -> throw IllegalArgumentException("Unknown op $input")
        }
        val monkeyB = input.substring(7, 11)
        return Monkey(monkeyA, monkeyB, op)
    }

    fun parseInput(input: List<String>): MutableMap<String, Either<Long, Monkey>> {
        return input.associate {
            val name = it.substring(0, 4)
            val value = it.substring(6)
            if (value[0].isDigit()) {
                name to Either.Left(value.toLong())
            } else {
                name to Either.Right(parseMonkey(value))
            }
        }.toMutableMap()
    }

    fun part1(input: List<String>): Long {
        val monkeys = parseInput(input)

        fun calc(monkeyName: String): Long {
            return when (val m = monkeys[monkeyName]!!) {
                is Either.Left -> m.value
                is Either.Right -> {
                    val monkey = m.value
                    val result = monkey.op.calc(calc(monkey.monkeyA), calc(monkey.monkeyB))
                    monkeys[monkeyName] = Either.Left(result)
                    result
                }
            }
        }

        return calc("root")
    }

    fun part2(input: List<String>): Long {
        val monkeys = parseInput(input)
        fun parentMonkeys(monkeyName: String): List<String> {
            if (monkeyName == "root") {
                return listOf()
            }
            val directParents =
                monkeys.filter { m -> m.value.isRight() && m.value.fold({ false }, { it.monkeyA == monkeyName || it.monkeyB == monkeyName }) }
                    .map { it.key }

            return directParents.flatMap { parentMonkeys(it) }.toList() + directParents
        }

        val pathToHumn = parentMonkeys("humn")

        fun calc(monkeyName: String): Long {
            return when (val m = monkeys[monkeyName]!!) {
                is Either.Left -> m.value
                is Either.Right -> {
                    val monkey = m.value
                    val result = monkey.op.calc(calc(monkey.monkeyA), calc(monkey.monkeyB))
                    if (monkeyName !in pathToHumn) monkeys[monkeyName] = Either.Left(result)
                    result
                }
            }
        }

        calc("root")

        val root = monkeys["root"]!! as Either.Right
        val otherValueName = if (root.value.monkeyA == pathToHumn[1]) {
            root.value.monkeyB
        } else {
            root.value.monkeyA
        }
        var otherValue = (monkeys[otherValueName]!! as Either.Left).value

        for (it in pathToHumn.drop(1)) {
            val monkey = monkeys[it]!! as Either.Right
            val monkeyA = monkeys[monkey.value.monkeyA]!!
            val monkeyB = monkeys[monkey.value.monkeyB]!!
            val op = monkey.value.op

            if (monkeyA is Either.Left && monkeyB is Either.Right<Monkey> || monkey.value.monkeyB == "humn") {
                val v = (monkeyA as Either.Left<Long>).value
                otherValue = when (op) {
                    MathOp.ADD -> otherValue - v
                    MathOp.SUB -> v - otherValue
                    MathOp.MUL -> otherValue / v
                    MathOp.DIV -> v / otherValue
                }
            } else if (monkeyB is Either.Left<Long> && monkeyA is Either.Right<Monkey> || monkey.value.monkeyA == "humn") {
                val v = (monkeyB as Either.Left<Long>).value
                otherValue = when (op) {
                    MathOp.ADD -> otherValue - v
                    MathOp.SUB -> otherValue + v
                    MathOp.MUL -> otherValue / v
                    MathOp.DIV -> otherValue * v
                }
            } else {
                throw IllegalArgumentException("Invalid monkey $it")
            }
        }

        return otherValue
    }

    val testInput = readInput("Day21_test")
    check(part1(testInput) == 152L)
    check(part2(testInput) == 301L)

    val input = readInput("Day21")
    println(part1(input))
    println(part2(input))
}
