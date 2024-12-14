import java.io.File

fun main() {
    //Day6.part1().let (::println)
    Day6.part2().let (::println)
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
            val newLoc = direction.applyTo(guardLoc)
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
        var guardOriginal = Location(0,0)
        input.forEachIndexed { rowIdx, row ->
            val colIdx = row.indexOfFirst { col -> col == '^' }
            if (colIdx >= 0) guardOriginal = Location(rowIdx, colIdx)
        }

        var guardLoc = guardOriginal
        var direction = Direction.UP
        val positions = mutableSetOf<Location>()
        for (i in 0..200000000) {
            positions.add(guardLoc)
            val newLoc = direction.applyTo(guardLoc)
            if (!newLoc.isInside(maxLine, maxCol)) break
            if (input[newLoc] == '#') {
                direction = direction.ninetyDegree()
            } else {
                guardLoc = newLoc
            }
        }

        var sum = 0
        positions.drop(1).forEach { adjloc ->
            val adjInput = input.map { it.clone() }.toMutableList()
            adjInput[adjloc.row][adjloc.col] = '#'

            guardLoc = guardOriginal
            direction = Direction.UP
            val landmarks = mutableSetOf<Pair<Location, Direction>>(guardLoc to direction)
            var newLandmark = Location(0, 0) to Direction.UP
            for (i in 0..200000000) {
                positions.add(guardLoc)
                val newLoc = direction.applyTo(guardLoc)
                if (!newLoc.isInside(maxLine, maxCol)) break
                if (adjInput[newLoc] == '#') {
                    direction = direction.ninetyDegree()
                    newLandmark = guardLoc to direction
                } else {
                    guardLoc = newLoc
                }
                if (landmarks.contains(guardLoc to direction)) {
                    sum += 1
                    break
                }
                landmarks.add(newLandmark)
            }
        }
        return sum
        // for each position adjusted
    }
}
