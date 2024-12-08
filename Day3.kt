import java.io.File

fun main() {
    //Day3.part1().let (::println)
    Day8.part2().let (::println)
    //Day3.solve().let(::println)
}

object Day3 : Day<Int, Int>() {
    val regex = Regex("(mul\\(\\d+,\\d+\\)|do\\(\\)|don't\\(\\))")
    val lines = File("input/3").readLines().fold("") { acc, s -> acc + s }
    private val matches = regex.findAll(lines).toMutableList()

    override fun part1(): Int {
        return matches.sumOf { match ->
            val numbers = match.value.drop(4).dropLast(1).split(",").map { it.toInt() }
            numbers[0] * numbers[1]
        }
    }

    override fun part2(): Int {
        var enabled = true
        return matches.sumOf { match ->
            if (match.value == "do()") {
                enabled = true; 0
            } else if (match.value == "don't()") {
                enabled = false; 0
            } else {
                if (enabled) {
                    val numbers = match.value.drop(4).dropLast(1).split(",").map { it.toInt() }
                    numbers[0] * numbers[1]
                } else 0
            }
        }
    }
}
