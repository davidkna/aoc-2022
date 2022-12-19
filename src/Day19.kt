import java.util.PriorityQueue

fun main() {
    class Blueprint(val id: Int, val ore: Int, val clay: Int, val obsidian: Pair<Int, Int>, val geode: Pair<Int, Int>) {
        fun eval(maxTime: Int): Int {
            val queue = PriorityQueue<List<Int>>(
                compareBy<List<Int>> {
                    val time = it[0]
                    val geodeBots = it[4]
                    val geode = it[8]
                    val timeLeft = maxTime - time
                    val potentialExtraBots = if (timeLeft > 1) (timeLeft + 1) * (timeLeft) / 2 else 0
                    geode + geodeBots * timeLeft + potentialExtraBots
                }.reversed()
            )
            queue.add(listOf(0, 1, 0, 0, 0, 0, 0, 0, 0))

            val maxOreBots = listOf(this.ore, this.clay, this.obsidian.first, this.geode.first).maxOrNull()!!
            val maxClayBots = this.obsidian.second
            val maxObsidianBots = this.geode.second
            val cache = mutableMapOf<List<Int>, Int>()

            while (queue.isNotEmpty()) {
                val item = queue.poll()
                val time = item[0]
                val oreBots = item[1]
                val clayBots = item[2]
                val obsidianBots = item[3]
                val geodeBots = item[4]
                val ore = item[5]
                val clay = item[6]
                val obsidian = item[7]
                val geode = item[8]

                val cacheKey = listOf(oreBots, clayBots, obsidianBots, geodeBots, ore, clay, obsidian, geode)
                if (cacheKey in cache) {
                    if (cache[cacheKey]!! <= time) continue
                } else {
                    cache[cacheKey] = time
                }

                if (time >= maxTime) {
                    return geode
                }

                val oreNextStep = ore + oreBots
                val clayNextStep = clay + clayBots
                val obsidianNextStep = obsidian + obsidianBots
                val geodeNextStep = geode + geodeBots
                val qSize = queue.size

                if (ore >= this.geode.first && obsidian >= this.geode.second) {
                    queue.add(listOf(time + 1, oreBots, clayBots, obsidianBots, geodeBots + 1, oreNextStep - this.geode.first, clayNextStep, obsidianNextStep - this.geode.second, geodeNextStep))
                    continue
                }

                if (oreBots <= maxOreBots && ore >= this.ore) {
                    queue.add(listOf(time + 1, oreBots + 1, clayBots, obsidianBots, geodeBots, oreNextStep - this.ore, clayNextStep, obsidianNextStep, geodeNextStep))
                }

                if (obsidianBots <= maxObsidianBots && ore >= this.obsidian.first && clay >= this.obsidian.second) {
                    queue.add(listOf(time + 1, oreBots, clayBots, obsidianBots + 1, geodeBots, oreNextStep - this.obsidian.first, clayNextStep - this.obsidian.second, obsidianNextStep, geodeNextStep))
                }

                if (clayBots <= maxClayBots && ore >= this.clay) {
                    queue.add(listOf(time + 1, oreBots, clayBots + 1, obsidianBots, geodeBots, oreNextStep - this.clay, clayNextStep, obsidianNextStep, geodeNextStep))
                }

                if (qSize + 3 != queue.size) {
                    queue.add(listOf(time + 1, oreBots, clayBots, obsidianBots, geodeBots, oreNextStep, clayNextStep, obsidianNextStep, geodeNextStep))
                }
            }
            return 0
        }
    }

    fun parseInput(input: List<String>): List<Blueprint> {
        val regex = Regex("Blueprint (\\d+): Each ore robot costs (\\d+) ore. Each clay robot costs (\\d+) ore. Each obsidian robot costs (\\d+) ore and (\\d+) clay. Each geode robot costs (\\d+) ore and (\\d+) obsidian.")
        return input.map {
            val (id, ore, clay, obsidian, clay2, geode, obsidian2) = regex.find(it)!!.destructured
            Blueprint(id.toInt(), ore.toInt(), clay.toInt(), obsidian.toInt() to clay2.toInt(), geode.toInt() to obsidian2.toInt())
        }
    }

    fun part1(input: List<String>): Int {
        val blueprints = parseInput(input)

        return blueprints.sumOf { blueprint -> blueprint.eval(24) * blueprint.id }
    }

    fun part2(input: List<String>): Int {
        val blueprints = parseInput(input)
        return blueprints.take(3).fold(1) { acc, blueprint -> acc * blueprint.eval(32) }
    }

    val testInput = readInput("Day19_test")
    check(part1(testInput) == 33)
    check(part2(testInput) == 56 * 62)

    val input = readInput("Day19")
    println(part1(input))
    println(part2(input))
}
