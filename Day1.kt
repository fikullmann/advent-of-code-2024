import java.io.File

fun main() {
    Day1.part1().let (::println)
    Day1.part2().let (::println)
}

object Day1 : Day<Int, Int>() {
    private val input = File("input/1_test").readLines().map { it.split("   ").map { it.toInt() } }
    val row1 = input.map { it[0] }.sorted()
    val row2 = input.map { it[1] }.sorted()

    override fun part1(): Int {
        var distances = 0
        for (i in row1.indices) {
            distances += distance(row1[i], row2[i])
        }
        return distances
    }

    override fun part2(): Int {
        val countMap = mutableMapOf<Int, Int>()
        row2.forEach { i ->
            countMap[i] = countMap.getOrDefault(i, 0) + 1
        }
        var simScore = 0
        row1.forEach { i ->
            simScore += i * countMap.getOrDefault(i, 0)
        }
        return simScore
    }
}
