import java.io.File
import kotlin.math.min

fun main() {
    var total = 0
    File("input/1").forEachLine {
        var value = ""
        for(i in it.indices){
            value += it[i].digitToIntOrNull() ?: matchDigit(it.substring(i..min(it.length -1, i+4)))
        }
        total += (value[0] +"" + value[value.length - 1]).toInt()
    }
    println(total)
/*    File("1")
            .readLines().sumOf {
                val value = ""
                val digitIter = it.filter { ch -> ch.isDigit() }.map { ch -> ch.digitToInt() }
                val first = digitIter.first()
                val last = digitIter.last()
                first * 10 + last
            }*/
}

fun matchDigit(string: String): String {
    var adjLine = string
    if (adjLine.length == 4) adjLine += "."
    if (adjLine.length == 3) adjLine += ".."
    return when {
        adjLine.matches(Regex("one..")) -> "1"
        adjLine.matches(Regex("two..")) -> "2"
        adjLine.matches(Regex("three")) -> "3"
        adjLine.matches(Regex("four.")) -> "4"
        adjLine.matches(Regex("five.")) -> "5"
        adjLine.matches(Regex("six..")) -> "6"
        adjLine.matches(Regex("seven")) -> "7"
        adjLine.matches(Regex("eight")) -> "8"
        adjLine.matches(Regex("nine.")) -> "9"
        else -> ""
    }
}
