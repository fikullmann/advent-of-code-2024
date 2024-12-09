import Day2.boolToInt
import java.io.File

fun main() {
    //Day4.part1().let (::println)
    Day4.part2().let (::println)
}

object Day4 : Day<Int, Int>() {
    private val input = File("input/4").readLines().map { it.toCharArray() }
    val findString = "XMAS"

    override fun part1(): Int {
        val directions = listOf(-1 to -1, -1 to 0, -1 to 1, 0 to -1, 0 to 1, 1 to -1, 1 to 0, 1 to 1)
        var result = 0
        input.forEachIndexed { rowIdx, row ->
            row.forEachIndexed { colIdx, value ->
                if (value == findString[0]) {
                    directions.forEach { dir ->
                        result += if (find(1, Location(rowIdx, colIdx).applyDistance(dir), dir)) 1 else 0
                    }
                    print("")
                }
            }
        }
        return result
    }

    fun find(strIdx: Int, currIdx: Location, dir: Pair<Int, Int>): Boolean {
        if (strIdx >= findString.length) return true
        if (!currIdx.isInside(input.size, input[0].size)) return false
        if (input[currIdx.row][currIdx.col] != findString[strIdx]) return false
        return find(strIdx + 1, currIdx.applyDistance(dir), dir)
    }

    override fun part2(): Int {
        val directions = listOf((-1 to -1) to (1 to 1), (-1 to 1) to (1 to -1))

        var result = 0
        input.forEachIndexed { rowIdx, row ->
            row.forEachIndexed { colIdx, value ->
                if (value == 'A') {
                    val loc = Location(rowIdx, colIdx)
                    var check: Int = 2
                    directions.forEach { dirPair ->
                        val set = mutableSetOf('S', 'M')
                        loc.applyDistance(dirPair.first).apply {
                            if (isInside(input.size, input[0].size)) {
                                set.remove(at(this))
                            }
                        }
                        loc.applyDistance(dirPair.second).apply {
                            if (isInside(input.size, input[0].size)) set.remove(at(this))
                        }
                        check -= set.isEmpty().boolToInt()
                    }
                    result += (check == 0).boolToInt()
                }
            }
        }
        return result
    }
    fun at(loc: Location) =
        input[loc.row][loc.col]
}
