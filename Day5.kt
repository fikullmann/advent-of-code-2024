import java.io.File
import kotlin.math.min

fun main() {
    day5Part2(File("input/5"))
}

fun day5Part1(content: File) {
    val lines = content.readLines()

    val allMaps: MutableList<List<List<Long>>> = mutableListOf()
    var oneMap: MutableList<List<Long>> = mutableListOf()
    content.forEachLine { line ->
        if (line.isEmpty()) { // next block starts
            allMaps.add(oneMap.toList())
            oneMap = mutableListOf()
        }
        if (line.isNotEmpty()) {
            if (line[0].isDigit()) {
                val mapLine = line.split(" ").mapNotNull { it.toLongOrNull() }
                assert(mapLine.size == 3)
                oneMap.add(mapLine)
            }
        }
    }
    allMaps.removeAt(0)

    val seedRanges: List<Long> = lines[0].split(": ", " ").drop(1).mapNotNull { it.toLongOrNull() }
    var minSeeds: Long = Long.MAX_VALUE
    seedRanges.forEach { seed ->
        var value: Long = seed

        allMaps.forEach { section ->
            var transformed = false
            for (mapLine in section.indices) {
                if (value in section[mapLine][1]..<section[mapLine][1] + section[mapLine][2]) {
                    value += (section[mapLine][0] - section[mapLine][1])
                    break
                }
            }
        }
        //println(value)
        minSeeds = min(value, minSeeds)
    }
    println(minSeeds)
}

fun day5Part2(content: File) {
    val lines = content.readLines()

    val allMaps: MutableList<List<List<Long>>> = mutableListOf()
    var oneMap: MutableList<List<Long>> = mutableListOf()
    content.forEachLine { line ->
        if (line.isEmpty()) { // next block starts
            allMaps.add(oneMap.toList())
            oneMap = mutableListOf()
        }
        if (line.isNotEmpty()) {
            if (line[0].isDigit()) {
                val mapLine = line.split(" ").mapNotNull { it.toLongOrNull() }
                assert(mapLine.size == 3)
                oneMap.add(mapLine)
            }
        }
    }
    allMaps.removeAt(0)

    val seedRanges: List<Long> = lines[0].split(": ", " ").drop(1).mapNotNull { it.toLongOrNull() }
    var minSeeds: Long = Long.MAX_VALUE
    for (i in seedRanges.indices step 2) {
        for (j in seedRanges[i]..<seedRanges[i] + seedRanges[i + 1]) {
            var value = j

            allMaps.forEach { section ->
                var transformed = false
                for (mapLine in section.indices) {
                    if (value in section[mapLine][1]..<section[mapLine][1] + section[mapLine][2]) {
                        value += (section[mapLine][0] - section[mapLine][1])
                        break
                    }
                }
            }
            //println(value)
            minSeeds = min(value, minSeeds)
        }
        println(minSeeds)
    }
    println(minSeeds)
}