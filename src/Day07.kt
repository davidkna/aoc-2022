fun main() {
    fun parseInput(input: List<String>): HashMap<String, Int> {
        val fs = HashMap<String, Int>()
        var pwd = ArrayDeque<String>()
        for (line in input) {
            if (line.startsWith("$ cd")) {
                when (val newLocation = line.removePrefix("$ cd ")) {
                    ".." -> {
                        pwd.removeLast()
                    }
                    "/" -> {
                        pwd = ArrayDeque()
                    }
                    else -> {
                        pwd.addLast(newLocation)
                    }
                }
            }
            val size = line.split(' ').first()
            if (size != "dir" && !size.startsWith("$")) {
                val pwdString = pwd.joinToString("/")
                if (fs.containsKey(pwdString)) {
                    fs[pwdString] = fs[pwdString]!! + size.toInt()
                } else {
                    fs[pwdString] = size.toInt()
                }
                for (i in pwd.indices) {
                    val parentPwdString = pwd.subList(0, i).joinToString("/")
                    if (fs.containsKey(parentPwdString)) {
                        fs[parentPwdString] = fs[parentPwdString]!! + size.toInt()
                    } else {
                        fs[parentPwdString] = size.toInt()
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
        val totalSize = fs[""]!!

        return fs.values.filter { totalSize - it < 40000000 }.min()
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 95437)
    check(part2(testInput) == 24933642)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}
