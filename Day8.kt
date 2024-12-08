import java.io.File

fun main() {
    Day8.part1().let (::println)
    Day8.part2().let (::println)
    //Day8.solve().let(::println)
}

object Day8 : Day<Int, Int>() {
    val lines = File("input/8").readLines()

    val locationMap: MutableMap<Char, MutableList<Location>> = buildMap<Char, MutableList<Location>> {
        lines.forEachIndexed { rowNr, row ->
            row.forEachIndexed { colNr, char ->
                if (char != '.') {
                    this.getOrPut(char, ::mutableListOf ).add(Location(rowNr, colNr))
                }
            }
        }
    }.toMutableMap()
    val maxRow = lines.size
    val maxCol = lines[0].length

    override fun part1(): Int {
        val antinodeLocation = mutableSetOf<Location>()
        locationMap.forEach { key, value ->
            value.cartesianProduct(value).filter { it.first != it.second }.forEach { (loc1, loc2) ->
                val distance = loc2.distance(loc1)
                antinodeLocation.add(loc1.applyDistance(distance, -1))
                antinodeLocation.add(loc1.applyDistance(distance, 2))
            }
        }
        return antinodeLocation.count { (row, col) -> row in 0..<maxRow && col in 0..<maxCol }
    }

    override fun part2(): Int {
        val isInside: Location.() -> Boolean = {
            row in 0..<maxRow && col in 0..<maxCol
        }
        val antinodeLocation = mutableSetOf<Location>()
        locationMap.forEach { key, value ->
            value.cartesianProduct(value).filter { it.first != it.second }.forEach { (loc1, loc2) ->
                val distance = loc2.distance(loc1)
                for (i in 0..200000) {
                    val newAntinode = loc1.applyDistance(distance, -i)
                        if (newAntinode.isInside()) antinodeLocation.add(newAntinode) else break
                }
                for (i in 0..200000) {
                    val newAntinode = loc2.applyDistance(distance, i)
                    if (newAntinode.isInside()) antinodeLocation.add(newAntinode) else break
                }
            }
        }
        return antinodeLocation.count()
    }
}


data class Location(val row: Int, val col: Int) {
    fun distance(other: Location): Distance = Distance(row - other.row, col - other.col)

    fun applyDistance(other: Distance, n: Int = 1): Location =
        Location(row = row + other.row * n, col = col + other.col * n)

}
data class Distance(val row: Int, val col: Int)


fun <T, U> Collection<T>.cartesianProduct(c2: Collection<U>): List<Pair<T, U>> {
    return flatMap { lhsElem -> c2.map { rhsElem -> lhsElem to rhsElem } }
}