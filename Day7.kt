import java.io.File
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.log

fun main() {
    Day7.part1().let (::println)
    Day7.part2().let (::println)
}

object Day7 : Day<Long, Long>() {
    val combinations = mutableMapOf<Int, List<List<Op>>>()
    val regex = Regex("\\d+")
    val lines = File("input/7").readLines()
    private val linesOfMatches = lines.map { line ->
        regex.findAll(line).map { it.value.toLong() }.toList()
    }

    override fun part1(): Long = linesOfMatches.sumOf { matches ->
            val lineResult = matches[0]

            if (plusRec(matches[1], matches.reversed().dropLast(2).toMutableList(), lineResult) ||
            plusMult(matches[1], matches.reversed().dropLast(2).toMutableList(), lineResult) ||
                plusConcat(matches[1], matches.reversed().dropLast(2).toMutableList(), lineResult)) {
                lineResult
            } else 0
        }

    private fun plusRec(first: Long, rest: List<Long>, result: Long): Boolean {
        if (first > result) return false
        if (rest.isEmpty()) {
            return if (first == result) true else false
        }
        val x = first + rest.last()
        return plusRec(x, rest.dropLast(1), result) || plusMult(x, rest.dropLast(1), result) || plusConcat(x, rest.dropLast(1), result)
    }
    private fun plusMult(first: Long, rest: List<Long>, result: Long): Boolean {
        if (first > result) return false
        if (rest.isEmpty()) {
            return if (first == result) true else false
        }
        val x = first * rest.last()
        return plusRec(x, rest.dropLast(1), result) || plusMult(x, rest.dropLast(1), result) || plusConcat(x, rest.dropLast(1), result)
    }
    private fun plusConcat(first: Long, rest: List<Long>, result: Long): Boolean {
        if (first > result) return false
        if (rest.isEmpty()) {
            return if (first == result) true else false
        }

        val x = first * Math.pow(10.0, floor(log(rest.last().toDouble(), 10.0))+1).toLong() + rest.last()
        return plusRec(x, rest.dropLast(1), result) || plusMult(x, rest.dropLast(1), result) || plusConcat(x, rest.dropLast(1), result)
    }


    override fun part2(): Long = linesOfMatches.sumOf { matches ->
        val lineResult = matches[0]

        if (plusRec(matches[1], matches.reversed().dropLast(2).toMutableList(), lineResult) ||
            plusMult(matches[1], matches.reversed().dropLast(2).toMutableList(), lineResult) ||
            plusConcat(matches[1], matches.reversed().dropLast(2).toMutableList(), lineResult)) {
            lineResult
        } else 0
    }

    fun combinations(n:Int){
        combinations[0] = listOf(listOf(Op.PLUS, Op.MULTIPLY))
        combinations[1] = listOf(listOf(Op.PLUS), listOf(Op.MULTIPLY))
        for (i in 2..n) {
            combinations[i] = combinations[i-1]!!.cartesianProductX(combinations[1]!!) // 2
            println(combinations[i])
        }
    }

    fun apply(numbers: List<Long>, operators: List<List<Op>>) {
        val result: Long = 0
        for (i in 1..<numbers.size) {

        }
    }
}


fun <T> Collection<List<T>>.cartesianProductX(c2: Collection<List<T>>): List<List<T>> {
    return flatMap { lhsElem -> c2.map { rhsElem -> lhsElem + rhsElem } }
}

enum class Op {
    PLUS,
    MULTIPLY,
}