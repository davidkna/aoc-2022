import kotlin.collections.ArrayDeque
fun main() {
    fun solution(input: List<String>, part2: Boolean): String {
        val stacks: List<ArrayDeque<Char>> = (0 until ((input[0].length + 1) / 4))
            .map { return@map ArrayDeque<Char>() }
            .toList()
        val initial = input.takeWhile { l -> l.isNotEmpty() }
        initial.forEach { l ->
            stacks.indices
                .asSequence()
                .filter { l[it * 4] == '[' }
                .forEach { stacks[it].addFirst(l[it * 4 + 1]) }
        }
        input.filter { l -> l.startsWith("move") }.forEach { l ->
            // move n from x to y
            val s = l.split(" ")
            val n = s[1].toInt()
            val x = s[3].toInt() - 1
            val y = s[5].toInt() - 1
            if (part2 && n != 1) {
                val t = ArrayDeque<Char>()
                for (i in 0 until n) {
                    t.addFirst(stacks[x].removeLast())
                }
                stacks[y].addAll(t)
            } else {
                for (i in 0 until n) {
                    stacks[y].addLast(stacks[x].removeLast())
                }
            }
        }
        return stacks.filter { s -> s.isNotEmpty() }.map { s -> s.removeLast() }.joinToString("")
    }

    val testInput = readInput("Day05_test")
    check(solution(testInput, false) == "CMZ")
    check(solution(testInput, true) == "MCD")

    val input = readInput("Day05")
    println(solution(input, false))
    println(solution(input, true))
}
