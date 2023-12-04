import java.io.File
import kotlin.math.max
import kotlin.math.min

fun main() {
    part2(File("input/3"))
}

fun parseNumbers(content: File):MutableList<Number> {
    val numbers: MutableList<Number> = mutableListOf()
    var lineNr = 0
    content.forEachLine {str ->
        str.mapIndexed() { idx, ch ->
            if (ch.code != 46) {
                if (ch.code in 48..57) {
                    if (!(idx > 0 && str[idx - 1].code in 48..57)) {
                        val endIndex = str.mapIndexedNotNull { idxF, char ->
                            if(idxF > idx && char.code !in 48..57) idxF else null
                        }.firstOrNull() ?: (idx + 3)
                        val number = str.subSequence(idx, min(endIndex, str.length))
                                .filter { num -> num.code in 48..57 }
                                .toString()
                                .toIntOrNull() ?: ch.toString().toInt() // finds the number in window
                        val size = number.toString().length
                        numbers.add(Number(lineNr, Pair(idx, idx+size-1), number))
                    }
                }
            }
        }
        lineNr++
    }
    return numbers
}

fun part1(content: File) {
    val numbers = parseNumbers(content)
    val charArray = content.readLines()
    var sum = 0
    numbers.forEach {nr ->
        var add = false
        val subList = charArray.subList(max(0, nr.line-1), min(nr.line+2, charArray.size))
        subList.forEach {str ->
            val toTest = str.subSequence(max(0, nr.range.first - 1), min(str.length, nr.range.second + 2))
            if (toTest.any { it.code !in 48..57 && it.code != 46 }) {
                add = true
            }
        }
        if (add) sum += nr.number
    }
    println(sum)
}
class Number(val line: Int, val range: Pair<Int, Int>, val number: Int = 0) {
}
class Star(val idx1:Int, val idx2: Int, var number:MutableList<Int> = mutableListOf()) {
}

fun part2(content: File) {
    val numbers = parseNumbers(content)
    val charArray = content.readLines()
    val stars: MutableList<Star> = mutableListOf()
    numbers.forEach {nr ->
        val found = false
        for (i in max(0, nr.line-1)..<min(nr.line+2, charArray.size)) {
            for (j in max(0, nr.range.first - 1)..<min(charArray[i].length, nr.range.second+2)) {
                if (charArray[i][j].code == 42 && !found) {
                    val foundStar = stars.find { it.idx1 == i && it.idx2 == j}
                            ?: Star(i, j).apply { stars.add(this) }
                    foundStar.number.add(nr.number)
                }
            }
        }
    }
    val sum = stars.map {
        if (it.number.size == 2) {
            it.number[0] * it.number[1]
        } else {
            0
        }
    }.sum()
    println(sum)
}