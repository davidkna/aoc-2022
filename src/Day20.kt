import java.util.LinkedList

fun main() {
    fun parseInput(input: List<String>) = LinkedList(input.mapIndexed { idx, n -> idx to n.toLong() })

    fun decrypt(out: LinkedList<Pair<Int, Long>>) {
        out.indices.forEach { idx ->
            val currentItem = out.withIndex().find { it.value.first == idx }!!
            val moves = currentItem.value.second

            if (moves == 0L) return@forEach

            out.removeAt(currentItem.index)
            out.add((currentItem.index + moves).mod(out.size), currentItem.value)
        }
    }

    fun calcSolution(list: LinkedList<Pair<Int, Long>>): Long {
        val zeroIndex = list.withIndex().find { it.value.second == 0L }!!.index
        return (1..3).sumOf { list[(zeroIndex + 1000 * it).mod(list.size)].second }
    }

    fun part1(input: List<String>): Long {
        val list = parseInput(input)
        decrypt(list)
        return calcSolution(list)
    }

    fun part2(input: List<String>): Long {
        val list = LinkedList(parseInput(input).map { it.first to it.second * 811589153 })
        (1..10).forEach { _ -> decrypt(list) }
        return calcSolution(list)
    }

    val testInput = readInput("Day20_test")
    check(part1(testInput) == 3L)
    check(part2(testInput) == 1623178306L)

    val input = readInput("Day20")
    println(part1(input))
    println(part2(input))
}
