import java.io.File

fun main() {
    Day2.solve().let(::println)
}

object Day2 : Day<Int, Int>() {
    private val limit = listOf(12, 13, 14)
    val input = File("input/2").readLines().mapIndexed {idx, str ->
        val listOfStrings = str.split(": ", "; ", ", ")
        (idx+1) to listOf(listOfStrings.filter { "red" in it }.maxOfOrNull { it.dropLast(4).toInt() } ?: Int.MAX_VALUE,
                listOfStrings.filter { "green" in it }.maxOfOrNull { it.dropLast(6).toInt() } ?: Int.MAX_VALUE,
                listOfStrings.filter { "blue" in it }.maxOfOrNull { it.dropLast(5).toInt() } ?: Int.MAX_VALUE)
    }

    override fun part1(): Int = input.fold(0) { acc, put ->
        val list = put.second
        acc + if ((list[0] <= limit[0]) && (list[1] <= limit[1]) && (list[2] <= limit[2])) put.first else 0
    }

    override fun part2(): Int = input.fold(0) { acc, listPair ->
        acc + (listPair.second[0] * listPair.second[1] * listPair.second[2])
    }
}
