fun main() {
    fun prio(a: Char): Int {
        if (a in 'a'..'z') return a - 'a' + 1
        if (a in 'A'..'Z') return a - 'A' + 27
        return 0
    }
    fun part1(input: List<String>): Int {
        return input.filter { return@filter it.isNotEmpty() }.sumOf {
            val firstHalf = it.slice(0 until it.length / 2)
                .map { c -> return@map prio(c) }
                .sorted()
            val secondHalf = it.slice((it.length / 2) until it.length)
                .map { c -> return@map prio(c) }
                .sorted()

            var i = 0
            var j = 0
            while (i < firstHalf.size && j < secondHalf.size) {
                if (firstHalf[i] < secondHalf[j]) {
                    i++
                } else if (firstHalf[i] > secondHalf[j]) {
                    j++
                } else {
                    return@sumOf firstHalf[i]
                }
            }
            return@sumOf 0
        }
    }

    fun part2(input: List<String>): Int {
        return input.filter { return@filter it.isNotEmpty() }.chunked(3).sumOf {
            val a = it[0].map { c -> return@map prio(c) }.sorted()
            val b = it[1].map { c -> return@map prio(c) }.sorted()
            val c = it[2].map { c -> return@map prio(c) }.sorted()

            var i = 0
            var j = 0
            var k = 0
            while (i < a.size && j < b.size && k < c.size) {
                if (a[i] == b[j] && b[j] == c[k]) {
                    return@sumOf a[i]
                } else if (a[i] <= b[j] && a[i] <= c[k]) {
                    i++
                } else if (b[j] <= a[i] && b[j] <= c[k]) {
                    j++
                } else {
                    k++
                }
            }
            return@sumOf 0
        }
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
