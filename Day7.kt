import java.io.File
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.log

fun main() {
    Day7.solve().let(::println)
}

object Day7 : Day<Long, Long>() {
    var concat: Boolean = false
    val regex = Regex("\\d+")
    val lines = File("input/7").readLines()
    private val linesOfMatches = lines.map { line ->
        regex.findAll(line).map { it.value.toLong() }.toList()
    }

    override fun part1(): Long = linesOfMatches.sumOf { matches ->
            val lineResult = matches[0]

            if (plusRec(matches[1], matches.reversed().dropLast(2).toMutableList(), lineResult) ||
            plusMult(matches[1], matches.reversed().dropLast(2).toMutableList(), lineResult)) {
                lineResult
            } else 0
        }

    private fun plusRec(first: Long, rest: List<Long>, result: Long): Boolean {
        if (first > result) return false
        if (rest.isEmpty()) {
            return if (first == result) true else false
        }
        val x = first + rest.last()
        return plusRec(x, rest.dropLast(1), result) ||
                plusMult(x, rest.dropLast(1), result) ||
                (if (concat) plusConcat(x, rest.dropLast(1), result) else false)
    }
    private fun plusMult(first: Long, rest: List<Long>, result: Long): Boolean {
        if (first > result) return false
        if (rest.isEmpty()) {
            return if (first == result) true else false
        }
        val x = first * rest.last()
        return plusRec(x, rest.dropLast(1), result) ||
                plusMult(x, rest.dropLast(1), result) ||
                (if (concat) plusConcat(x, rest.dropLast(1), result) else false)
    }
    private fun plusConcat(first: Long, rest: List<Long>, result: Long): Boolean {
        if (first > result) return false
        if (rest.isEmpty()) {
            return if (first == result) true else false
        }
        val x = first * Math.pow(10.0, floor(log(rest.last().toDouble(), 10.0))+1).toLong() + rest.last()
        return plusRec(x, rest.dropLast(1), result) ||
                plusMult(x, rest.dropLast(1), result) ||
                (if (concat) plusConcat(x, rest.dropLast(1), result) else false)
    }


    override fun part2(): Long {
        concat = true
        return linesOfMatches.sumOf { matches ->
            val lineResult = matches[0]

            if (plusRec(matches[1], matches.reversed().dropLast(2).toMutableList(), lineResult) ||
                plusMult(matches[1], matches.reversed().dropLast(2).toMutableList(), lineResult) ||
                plusConcat(matches[1], matches.reversed().dropLast(2).toMutableList(), lineResult)
            ) {
                lineResult
            } else 0
        }
    }
}