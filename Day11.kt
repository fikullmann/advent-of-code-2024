import java.io.File
import kotlin.math.abs

fun main() {
    day11Part1(File("input/11").readLines())
}

fun day11Part1(lines:List<String>) {
    val galaxies = mutableListOf<Pair<Int, Int>>()
    val emptyRows = mutableListOf<Int>()
    val emptyColumns = lines[0].indices.toMutableList()

    val paths = mutableListOf<Int>()

    lines.forEachIndexed { lineIdx, line ->
        val galaxiesFound = line.mapIndexedNotNull { idx, elem -> idx.takeIf { elem == '#' } }
        galaxiesFound.forEach {columnIdx ->
            galaxies.add(lineIdx to columnIdx)
            emptyColumns.remove(columnIdx)
        }
        if (galaxiesFound.isEmpty()) {
            emptyRows.add(lineIdx)
        }
    }
    0..galaxies.size

    for (i in galaxies.indices) {
        for (j in i+1..<galaxies.size) {
            var shortestPath = abs(galaxies[i].first - galaxies[j].first) +
                    abs(galaxies[i].second - galaxies[j].second)
            emptyRows.forEach {
                if (it in (galaxies[i].first..galaxies[j].first) || it in (galaxies[j].first..galaxies[i].first)) shortestPath+=999999
            }
            emptyColumns.forEach {
                if (it in galaxies[i].second..galaxies[j].second || it in galaxies[j].second..galaxies[i].second) shortestPath+=999999
            }
            paths.add(shortestPath)
        }
    }
    println(galaxies)
    println(paths)
    println(paths.sumOf { it.toLong() })

}