import java.io.File

fun main() {
    Day1.part2().let(::println)
}

object Day1 : Day<Int, Int>() {
    private val input = File("input/1").readLines()
    private val numberMap = mapOf(
            "one" to 1,
            "two" to 2,
            "three" to 3,
            "four" to 4,
            "five" to 5,
            "six" to 6,
            "seven" to 7,
            "eight" to 8,
            "nine" to 9,
    )

    override fun part1(): Int = input.sumOf { str ->
        10 * str.first { it.isDigit() }.digitToInt() + str.last { it.isDigit() }.digitToInt()
    }

    override fun part2(): Int = input.sumOf { str ->
        val fNr = str.indexOfFirst { it.isDigit() }
        val lNr = str.indexOfLast { it.isDigit() }
        val fText = str.findAnyOf(numberMap.keys) ?: (Int.MAX_VALUE to "")
        val lText = str.findLastAnyOf(numberMap.keys) ?: (Int.MIN_VALUE to "")

        (10 * if (fNr < fText.first) str[fNr].digitToInt() else numberMap[fText.second]!!) +
                if (lNr > lText.first) str[lNr].digitToInt() else numberMap[lText.second]!!
    }
}
