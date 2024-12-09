import java.io.File
import kotlin.math.floor
import kotlin.math.log
import kotlin.math.pow

fun main() {
    //Day7.part1().let(::println)
    //Day7.part2().let(::println)
    Day7.solve().let(::println)
}

object Day7 : Day<Long, Long>() {
    val regex = Regex("\\d+")
    val lines = File("input/7").readLines()
    private val linesOfMatches = lines.map { line ->
        regex.findAll(line).map { it.value.toLong() }.toList()
    }

    override fun part1(): Long = linesOfMatches.sumOf { matches ->
        if (opRec(matches[1], matches.reversed().dropLast(2), matches[0], listOf(plusL, multL))) {
            matches[0]
        } else 0
    }

    fun opRec(first: Long, rest: List<Long>, result: Long, functions: List<(Long, Long) -> Long>) : Boolean {
        if (first > result) return false
        if (rest.isEmpty()) {
            return if (first == result) true else false
        }
        return functions.any { fn ->
            opRec(fn(first, rest.last()), rest.dropLast(1), result, functions)
        }
    }

    val plusL = { fst: Long, snd: Long -> fst + snd }
    val multL = { fst: Long, snd: Long -> fst * snd }
    val concatL = { fst: Long, snd: Long -> fst * 10.0.pow(snd.length().toDouble()).toLong() + snd }

    override fun part2(): Long = linesOfMatches.sumOf { matches ->
        if (opRec(matches[1], matches.reversed().dropLast(2), matches[0], listOf(plusL, multL, concatL))) {
            matches[0]
        } else 0
    }
}