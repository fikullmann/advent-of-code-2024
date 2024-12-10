import Day2.boolToInt
import java.io.File

fun main() {
    Day10.part2().let (::println)
    //Day10.part1("10_test2").let (::println)
    //Day10.part1("10_test3").let (::println)
    //Day10.part2().let (::println)
}

object Day10 : Day<Int, Int>() {
    private var input = File("input/10").readLines().map { it.map(Char::digitToInt) }
    val maxLine = input.size
    val maxCol = input[0].size

    val trailheads = buildList {
        input.forEachIndexed { lineIdx , line ->
            line.forEachIndexed { colIdx, col ->
                if (col == 0) this.add(Location(lineIdx, colIdx))
            }
        }
    }

    override fun part1(): Int = trailheads.sumOf { searchTrail(0, it).size }

    operator fun List<List<Int>>.get(loc: Location) = this[loc.row][loc.col]

    private fun searchTrail(height: Int, loc: Location): Set<Location> {
        if (!loc.isInside(maxLine, maxCol) || input[loc] != height) return emptySet()
        if (height == 9) return setOf(loc)

        return listOf(loc::left, loc::right, loc::up, loc::down).fold(emptySet<Location>()) { acc, fn ->
            acc.union(searchTrail(height + 1, fn()))
        }
    }

    private fun searchDistinctTrail(height: Int, loc: Location): Int {
        if (!loc.isInside(maxLine, maxCol) || input[loc] != height) return 0
        if (height == 9) return 1

        return listOf(loc::left, loc::right, loc::up, loc::down).sumOf { fn ->
            searchDistinctTrail(height + 1, fn())
        }
    }

    override fun part2(): Int = trailheads.sumOf { searchDistinctTrail(0, it) }
}
