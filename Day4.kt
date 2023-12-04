import java.io.File
import kotlin.math.min

fun main() {
    day4Part2(File("input/4"))
}

fun day4Part1(content: File) {
    var total = 0
    content.forEachLine { line ->

        val listOfStrings = line.split(": ", " | ")
        // Winning numbers
        val winningNumbers = listOfStrings[1].trim().split(Regex(" +"))
        val drawnNumbers = listOfStrings[2].trim().split(Regex(" +"))
        total += drawnNumbers.foldRight(0) { nr, acc ->
            val trimmedNr = nr.trim()
            if (winningNumbers.contains(trimmedNr)) {
                if (acc == 0) 1 else acc * 2
            } else acc
        }
        //println(total)
    }
    println(total)
}


fun day4Part2(content: File) {
    val genScratch: MutableList<Int> = mutableListOf()
    val totalLines = content.readLines().size
    repeat(totalLines) { genScratch.add(1) }
    var lineCount = 0
    content.forEachLine { line ->
        var total = 0
        val listOfStrings = line.split(": ", " | ")
        // Winning numbers
        val winningNumbers = listOfStrings[1].trim().split(Regex(" +"))
        val drawnNumbers = listOfStrings[2].trim().split(Regex(" +"))
        total += winningNumbers.intersect(drawnNumbers.toSet()).size
        for (i in lineCount+1..min(lineCount+total, totalLines -1)) {
            genScratch[i] += genScratch[lineCount]
        }
        //println(total)
        lineCount++
    }
    println(genScratch.sum())
}