import java.io.File
import kotlin.math.min

fun main() {
    //day10Part1(File("input/10_test1").readLines())
    //day10Part1(File("input/10_test2").readLines())
    day10Part2(File("input/10").readLines())

}

fun day10Part1(lines: List<String>) {
    var max = 0
    val connector = Connector(lines)
    val list: MutableList<Connection> = mutableListOf()
    lines.forEachIndexed { lineIdx, line ->
        val columnIdx = line.indexOf('S')
        if (columnIdx >= 0) {
            list.add(0, Connection(lineIdx to columnIdx))
        }
    }
    while (list.isNotEmpty()) {
        val con = list.removeFirst()
        if (con.max > max) {
            max = con.max
        }
        list.addAll(connector.checkConnecting(con))
    }
    println(max / 2)
}

fun day10Part2(lines: List<String>) {
    var total = 0
    val connector = Connector(lines)
    val list: MutableList<Connection> = mutableListOf()
    lines.forEachIndexed { lineIdx, line ->
        val columnIdx = line.indexOf('S')
        if (columnIdx >= 0) {
            list.add(0, Connection(lineIdx to columnIdx))
        }
    }
    while (list.isNotEmpty()) {
        val con = list.removeFirst()
        list.addAll(connector.checkConnecting(con))
    }
    // now we have the start of the loop yay
    // I'm writing comments because am braindead

    var curr = connector.first?.prev
    val loopList = mutableListOf<Pair<Int, Int>>()
    var firstDir: Dir = Dir.NORTH
    while (curr != null) {
        loopList.add(curr.idx)
        firstDir = curr.dir ?: firstDir
        curr = curr.prev
    }
    if (firstDir == Dir.NORTH) firstDir = Dir.SOUTH
    else if (firstDir == Dir.SOUTH) firstDir = Dir.NORTH
    val idxList = loopList.filter { it -> lines[it.first][it.second] != '-' }

    lines.forEachIndexed { lineIdx, line ->
        val editLine = line.toMutableList()
        line.forEachIndexed { columnIdx, _ ->
            if (!loopList.contains(lineIdx to columnIdx)) {
                if (connector.checkInside(idxList.filter { it.first == lineIdx }, columnIdx, firstDir)) {
                    editLine[columnIdx] = 'I'
                    total++
                }
            }
        }
        //println(String(editLine.toCharArray()))
    }
    println(total)
}

// L7 FJ |
// F7 LJ

class Connection(
        val idx: Pair<Int, Int>,
        val max: Int = 0,
        val dir: Dir? = null,
        val prev: Connection? = null
)

class Connector(private val lines: List<String>) {
    var first: Connection? = null
    fun checkConnecting(
            con: Connection,
    ): List<Connection> {
        val values: MutableList<Connection> = mutableListOf()
        val idx = con.idx
        val max = con.max
        val dir = con.dir

        if (idx.first >= 0 && idx.second >= 0 && idx.first < lines.size && idx.second < lines[0].length) {
            //println("${curr.first} " + curr.second + "${lines[curr.first][curr.second]} $max")
            when (lines[idx.first][idx.second]) {
                'S' -> {
                    if (max == 0) {
                        values.add(Connection(idx.first + 1 to idx.second, max + 1, dir = Dir.NORTH, con))
                        values.add(Connection(idx.first - 1 to idx.second, max + 1, dir = Dir.SOUTH, con))
                        values.add(Connection(idx.first to idx.second + 1, max + 1, dir = Dir.EAST, con))
                        values.add(Connection(idx.first to idx.second - 1, max + 1, dir = Dir.WEST, con))
                    } else {
                        first = con
                    }
                }

                '|' -> {
                    if (dir == Dir.NORTH) values.add(Connection(idx.first + 1 to idx.second, max + 1, dir = Dir.NORTH, con))
                    if (dir == Dir.SOUTH) values.add(Connection(idx.first - 1 to idx.second, max + 1, dir = Dir.SOUTH, con))
                }

                '-' -> {
                    if (dir == Dir.WEST) values.add(Connection(idx.first to idx.second + 1, max + 1, dir = Dir.WEST, con))
                    if (dir == Dir.EAST) values.add(Connection(idx.first to idx.second - 1, max + 1, dir = Dir.EAST, con))
                }

                'L' -> {
                    if (dir == Dir.EAST) values.add(Connection(idx.first - 1 to idx.second, max + 1, dir = Dir.SOUTH, con))
                    if (dir == Dir.NORTH) values.add(Connection(idx.first to idx.second + 1, max + 1, dir = Dir.WEST, con))
                }

                'J' -> {
                    if (dir == Dir.WEST) values.add(Connection(idx.first - 1 to idx.second, max + 1, dir = Dir.SOUTH, con))
                    if (dir == Dir.NORTH) values.add(Connection(idx.first to idx.second - 1, max + 1, dir = Dir.EAST, con))

                }

                '7' -> {
                    if (dir == Dir.WEST) values.add(Connection(idx.first + 1 to idx.second, max + 1, dir = Dir.NORTH, con))
                    if (dir == Dir.SOUTH) values.add(Connection(idx.first to idx.second - 1, max + 1, dir = Dir.EAST, con))

                }

                'F' -> {
                    if (dir == Dir.EAST) values.add(Connection(idx.first + 1 to idx.second, max + 1, dir = Dir.NORTH, con))
                    if (dir == Dir.SOUTH) values.add(Connection(idx.first to idx.second + 1, max + 1, dir = Dir.WEST, con))
                }

                '.' -> {
                    return values
                }

                else -> return values
            }
        }
        return values
    }

    fun checkInside(line: List<Pair<Int, Int>>, columnIdx: Int, firstDir: Dir): Boolean {
        var frontCheck = false
        var backCheck = false
        if (columnIdx > 0) {
            frontCheck = checkSub(line.filter { it.second in (0..<columnIdx)}, firstDir)
        }
        if (columnIdx < lines[0].length) {
            backCheck = checkSub(line.filter { it.second in (columnIdx+1..<lines[0].length)}, firstDir)
        }
        return frontCheck && backCheck
    }

    private fun checkSub(line: List<Pair<Int, Int>>, firstDir: Dir): Boolean {
        val stringLine = line.map {
            lines[it.first][it.second]
        }
        var north = stringLine.count { it == 'L' || it == 'J' }
        var south = stringLine.count { it == 'F' || it == '7' }
        var straight = stringLine.count { it == '|' }
        if (stringLine.contains('S')) {
            val first = listOf(first?.dir, firstDir)
            if (first.contains(Dir.NORTH) && first.contains(Dir.SOUTH)) straight++
            else if (first.contains(Dir.NORTH)) north++
            else if (first.contains(Dir.SOUTH)) south++
        }
        return ((min(north, south) + straight) % 2 == 1)
    }
}



