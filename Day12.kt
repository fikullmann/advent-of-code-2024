import Day2.boolToInt
import java.io.File

fun main() {
    Day12.part2().let (::println)
    //Day10.part2().let (::println)
}

object Day12 : Day<Long, Long>() {
    private var input = File("input/12").readLines().map { it.toCharArray() }
    val maxLine = input.size
    val maxCol = input[0].size

    val plantMap = buildMap<Char, MutableList<Location>> {
        input.forEachIndexed { rowIdx, row ->
            row.forEachIndexed { colIdx, col ->
                getOrPut(col, ::mutableListOf).add(Location(rowIdx, colIdx))
            }
        }
    }

    override fun part1(): Long {
        var sum = 0L
        plantMap.forEach { (plantKey, locations) ->
            val locationCache = locations.toMutableList()
            while (locationCache.isNotEmpty()) {
                val areas = mutableSetOf<Location>()
                val perimeter = calculateAP(plantKey, locationCache.first(), areas)
                sum += areas.size * perimeter
                locationCache.removeAll(areas)
            }
        }
        return sum
    }

    fun calculateAP(key: Char, loc: Location, locations: MutableSet<Location>): Long {
        if (!loc.isInside(maxLine, maxCol)) return 0
        if (input[loc] != key) return 0
        else {
            if (locations.contains(loc)) return 0
            locations.add(loc)
            var sum = 0L
            sum += listOf(loc.left(), loc.right(), loc.up(), loc.down()).sumOf { newLoc ->
                if (!newLoc.isInside(maxLine, maxCol) || input[newLoc] != key)
                    1L
                else 0L
            }
            sum += listOf(loc.left(), loc.right(), loc.up(), loc.down()).sumOf { newLoc ->
                calculateAP(key, newLoc, locations)
            }
            return sum
        }
    }

    operator fun List<CharArray>.get(loc: Location) = this[loc.row][loc.col]


    override fun part2(): Long {
        var sum = 0L
        plantMap.forEach { (plantKey, locations) ->
            val locationCache = locations.toMutableList()
            while (locationCache.isNotEmpty()) {
                val areas = mutableSetOf<Location>()
                calculateAP(plantKey, locationCache.first(), areas)

                //[a,b)
                val perimeter = findPerimeter(areas)

                sum += areas.size * perimeter
                locationCache.removeAll(areas)
            }
        }
        return sum
    }

    fun findPerimeter(locations: MutableSet<Location>): Int {
        val plant = input[locations.first()]
        val minRow = locations.minOf { it.row }
        val maxRow = locations.maxOf { it.row }
        val minCol = locations.minOf { it.col }
        val maxCol = locations.maxOf { it.col }

        var sumHorizontal = 0
        var old = listOf<Int>()
        for (i in minRow..maxRow) {
            val new = mutableListOf<Int>()
            var inn = false
            for (j in minCol..maxCol) {
                if (!inn && locations.contains(Location(i,j))) {
                    inn = true
                    new.add(j)
                } else if (inn && !locations.contains(Location(i,j))) {
                    inn = false
                    new.add(j)
                }
                if (j == maxCol && inn) {
                    new.add(maxCol+1)
                }
            }

            sumHorizontal += new.size
            old.forEachIndexed { idx, nr ->
                sumHorizontal -= (new.indexOfFirst { it == nr} %2 == idx%2).boolToInt()
            }
            old = new
        }

        old = listOf()
        for (j in minCol..maxCol) {
            val new = mutableListOf<Int>()
            var inn = false
            for (i in minRow..maxRow) {
                if (!inn && locations.contains(Location(i,j))) {
                    inn = true
                    new.add(i)
                } else if (inn && !locations.contains(Location(i,j))) {
                    inn = false
                    new.add(i)
                }
                if (i == maxRow && inn) {
                    new.add(maxRow+1)
                }
            }

            sumHorizontal += new.size
            old.forEachIndexed { idx, nr ->
                sumHorizontal -= (new.indexOfFirst { it == nr} %2 == idx%2).boolToInt()
            }
            old = new
        }
        return sumHorizontal
    }
}
