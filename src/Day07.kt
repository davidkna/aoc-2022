fun main() {
    fun parseInput(input: List<String>): HashMap<List<String>, Int> {
        val fs = HashMap<List<String>, Int>()
        var pwd = ArrayList<String>()
        for (line in input) {
            if (line.startsWith("$ cd")) {
                when (val newLocation = line.removePrefix("$ cd ")) {
                    ".." -> {
                        pwd.removeLast()
                    }
                    "/" -> {
                        pwd = ArrayList()
                    }
                    else -> {
                        pwd.add(newLocation)
                    }
                }
            }
            val size = line.split(' ').first()
            if (size != "dir" && !size.startsWith("$")) {
                for (i in 0..pwd.size) {
                    val cursor = pwd.subList(0, i).toList()
                    if (fs.containsKey(cursor)) {
                        fs[cursor] = fs[cursor]!! + size.toInt()
                    } else {
                        fs[cursor] = size.toInt()
                    }
                }
            }
        }
        return fs
    }

    fun part1(input: List<String>): Int {
        val fs = parseInput(input)

        return fs.values.filter { it < 100000 }.sum()
    }

    fun part2(input: List<String>): Int {
        val fs = parseInput(input)
        val totalSize = fs[listOf()]!!

        return fs.values.filter { totalSize - it < 40000000 }.min()
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 95437)
    check(part2(testInput) == 24933642)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}
