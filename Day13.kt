import Day2.boolToInt
import java.io.File

fun main() {
    Day13.part1().let (::println)
    Day13.part2().let (::println)
}

object Day13 : Day<Long, Long>() {
    val regex = Regex("\\d+")
    private var input = File("input/13").readLines().fold("") { acc, s -> acc + s }
    private val matches = regex.findAll(input).toMutableList()

    override fun part1(): Long {
        var sum = 0L
        for (i in 0..<matches.size step 6) {
            val aX = matches[i].value.toLong()
            val aY = matches[i + 1].value.toLong()

            val bX = matches[i + 2].value.toLong()
            val bY = matches[i + 3].value.toLong()
            val targetX = matches[i + 4].value.toLong()
            val targetY = matches[i + 5].value.toLong()

            val a = (aX * bY) - (aY * bX)
            val b = (targetX * bY) - (targetY * bX)

            val aRes = b.toDouble()/a
            val bRes = (targetY - aY * aRes) / bY
            if (aRes < 0 || bRes < 0 || !aRes.rem(1).equals(0.0) || !bRes.rem(1).equals(0.0)) {
                sum += 0
            } else {
                sum += (aRes * 3 + bRes).toLong()
            }
        }
        return sum
    }

    override fun part2(): Long {
        var sum = 0L
        for (i in 0..<matches.size step 6) {
            val aX = matches[i].value.toLong()
            val aY = matches[i + 1].value.toLong()

            val bX = matches[i + 2].value.toLong()
            val bY = matches[i + 3].value.toLong()
            val targetX = matches[i + 4].value.toLong() + 10000000000000
            val targetY = matches[i + 5].value.toLong() + 10000000000000

            val a = (aX * bY) - (aY * bX)
            val b = (targetX * bY) - (targetY * bX)

            val aRes = b.toDouble()/a
            val bRes = (targetY - aY * aRes) / bY
            if (aRes < 0 || bRes < 0 || !aRes.rem(1).equals(0.0) || !bRes.rem(1).equals(0.0)) {
                sum += 0
            } else {
                sum += (aRes * 3 + bRes).toLong()
            }
        }
        return sum
    }
}
