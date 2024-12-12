import java.io.File

fun main() {
    Day6.part1().let (::println)
    //Day10.part1("10_test2").let (::println)
    //Day10.part1("10_test3").let (::println)
    //Day10.part2().let (::println)
}

object Day6 : Day<Int, Int>() {
    private var input = File("input/6").readLines().map { it.toCharArray() }
    val maxLine = input.size
    val maxCol = input[0].size

    operator fun List<CharArray>.get(loc: Location) = this[loc.row][loc.col]
    override fun part1(): Int {
        var guardLoc = Location(0,0)
        input.forEachIndexed { rowIdx, row ->
            val colIdx = row.indexOfFirst { col -> col == '^' }
            if (colIdx >= 0) guardLoc = Location(rowIdx, colIdx)
        }
        var direction = Direction.UP

        val positions = mutableSetOf<Location>()
        for (i in 0..200000000) {
            positions.add(guardLoc)
            val newLoc = when (direction) {
                Direction.UP -> guardLoc.up()
                Direction.DOWN -> guardLoc.down()
                Direction.LEFT -> guardLoc.left()
                Direction.RIGHT -> guardLoc.right()
            }
            if (!newLoc.isInside(maxLine, maxCol)) break
            if (input[newLoc] == '#') {
                direction = direction.ninetyDegree()
            } else {
                guardLoc = newLoc
            }
        }
        return positions.size
    }

    operator fun List<List<Int>>.get(loc: Location) = this[loc.row][loc.col]


    override fun part2(): Int {
TODO()
    }
}
