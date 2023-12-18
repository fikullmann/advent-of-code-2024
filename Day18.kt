import java.io.File
import java.util.*
import kotlin.math.abs

fun main() {
    Day18.part1().let(::println)
    Day182.part2().let(::println)
}

object Day182 : Day<Int, Long>() {

    private val input: List<DigPlan> = File("input/18").readLines().map { str ->
        val (dirStr, value, hex) = str.split(" ")
        val trimmed = hex.trim('(', ')')
        DigPlan(when (trimmed.last()) {
            '0' -> 0 to 1
            '2' -> 0 to -1
            '3' -> -1 to 0
            '1' -> 1 to 0
            else -> throw ExceptionInInitializerError()
        }, Integer.decode(trimmed.dropLast(1)))
    }

    /*val (dirStr, value, hex) = str.split(" ")
    DigPlan(when (dirStr) {
        "R" -> 0 to 1
        "L" -> 0 to -1
        "U" -> -1 to 0
        "D" -> 1 to 0
        else -> throw ExceptionInInitializerError()
    }, value.toInt())
}*/
    val landings = mutableListOf<LandingMark>()

    data class DigPlan(
            val dir: Pair<Int, Int>,
            val value: Int,
    )

    data class LandingMark(val lIdx: Int, val cIdx: Int, val mark: Int) {
        /**
         * @param dir, if 1 = south, if -1 = north
         */
    }

    enum class Mark() {
        NORTH, SOUTH
    }

    fun create(lIdx1: Int, cIdx1: Int, lIdx2: Int, cIdx2: Int, dir: Int) {
        landings.add(LandingMark(lIdx1, cIdx1, dir))
        landings.add(LandingMark(lIdx2, cIdx2, -dir))
    }

    override fun part1(): Int {
        return 0
    }

    override fun part2(): Long {
        var f1 = 0
        var s1 = 0
        //calculate Landing
        input.forEach { dp ->
            val f2 = dp.dir.first * dp.value + f1
            val s2 = dp.dir.second * dp.value + s1
            if (dp.dir.first != 0) {
                create(f1, s1, f2, s2, dp.dir.first)
            }
            f1 = f2
            s1 = s2
        }

        val groupedLandings = landings.groupBy { it.lIdx }
        val indices = landings.map { it.lIdx }.sorted().distinct()
        var sum: Long = 0
        var prevIdx = 0
        val currLandings = mutableListOf<IntRange>()
        indices.forEach { idx ->
            if (idx == -6487589) {
                "helo"
            }
            val landingsOfIdx = groupedLandings[idx]!!.sortedBy { it.cIdx }
            sum += currLandings.sumOf {
                it.size().toLong() * (idx - prevIdx).toLong()
            }
            for (i in 1..landingsOfIdx.size step 2) {
                val l1 = landingsOfIdx[i-1]
                val l2 = landingsOfIdx[i]

                // NN -> only add this range, split existing range into smaller ranges
                if (l1.mark+l2.mark == 2) { // SS -> new range has to be added
                    val overlap = currLandings.find { range ->
                        (l1.cIdx..l2.cIdx).all { it in range }
                    }
                    if (overlap != null) {
                        currLandings.remove(overlap)
                        currLandings.add(overlap.first..l1.cIdx)
                        currLandings.add(l2.cIdx..overlap.last)
                        sum += (l1.cIdx+1..l2.cIdx-1).size()
                    } else {
                        currLandings.add(l1.cIdx..l2.cIdx)
                    }
                }
                else if (l1.mark+l2.mark == 0) { // NS -> only add this range, make existing range smaller
                    val fix: IntRange = currLandings.find { l1.cIdx in it || l2.cIdx in it }!!
                    currLandings.remove(fix)

                    if (l1.cIdx == fix.last) {
                        currLandings.add(fix.first..l2.cIdx)
                    } else if (l2.cIdx == fix.first) {
                        currLandings.add(l1.cIdx..fix.last)
                    } else if (l1.cIdx == fix.first) { // remove from range
                        currLandings.add(l2.cIdx..fix.last)
                        sum += (l1.cIdx..<l2.cIdx).size()
                    } else if (l2.cIdx == fix.last) { // remove from range
                        sum += (l1.cIdx..<l2.cIdx).size()
                        currLandings.add(fix.first..l1.cIdx)
                    }
                }
                else if (l1.mark+l2.mark < 0) {
                    val fix: IntRange? = currLandings.find { l1.cIdx in it && l2.cIdx in it}
                    if (fix == null) {
                        val earlier: IntRange = currLandings.find { l1.cIdx == it.last}!!
                        val later: IntRange = currLandings.find { l2.cIdx == it.first}!!
                        if (earlier != later) {
                            currLandings.remove(earlier)
                            currLandings.remove(later)
                            currLandings.add(earlier.first..later.last)
                        } else {
                            sum += earlier.size()
                            currLandings.remove(earlier)
                        }
                    } else {
                        sum += fix.size()
                        currLandings.remove(fix)
                        if (fix.first != l1.cIdx) {
                            currLandings.add(fix.first..l1.cIdx)
                        }
                        if (fix.last != l2.cIdx) {
                            currLandings.add(l2.cIdx..fix.last)
                        }
                    }
                }
            }
            prevIdx = idx
        }
        return sum
    }

    fun List<Int>.asRange(): IntRange {
        val range = first()..last()
        if (range.toList() != this) {
            throw IllegalStateException("list is not continuous.")
        }
        return range
    }
    fun IntRange.size(): Int = abs(this.last - this.first) + 1



}

object Day18 : Day<Int, Int>() {
    private val input: List<DigPlan> = File("input/18_test").readLines().map { str ->
        val (dirStr, value, hex) = str.split(" ")
        DigPlan(when (dirStr) {
            "R" -> Dir.EAST
            "L" -> Dir.WEST
            "U" -> Dir.NORTH
            "D" -> Dir.SOUTH
            else -> throw ExceptionInInitializerError()
        }, value.toInt(), hex)
    }

    //val terrain = mutableMapOf<Pair<Int, Int>, Terrain>().withDefault { Terrain.EMPTY }
    val terrain2 = Array<Array<Terrain>>(20) {
        Array(20) { Terrain.EMPTY }
    }

    override fun part1(): Int {
        /*terrain2[190][200] = Terrain.H
        var currIdx = 190 to 200*/

        terrain2[0][0] = Terrain.H
        var currIdx = 0 to 0

        input.forEach { dp ->
            println(dp.value)
            when (dp.dir) {
                Dir.EAST -> {
                    digEast(currIdx, dp.value)
                    currIdx += 0 to dp.value
                }

                Dir.WEST -> {
                    currIdx += 0 to -dp.value
                    digEast(currIdx + (0 to -1), dp.value)
                }

                Dir.SOUTH -> {
                    terrain2[currIdx.first][currIdx.second] = Terrain.V
                    digSouth(currIdx, dp.value)
                    currIdx += dp.value to 0
                }

                Dir.NORTH -> {
                    currIdx += -dp.value to 0
                    terrain2[currIdx.first][currIdx.second] = Terrain.VN
                    println("${currIdx.first} ${currIdx.second}")
                    digSouth(currIdx + (-1 to 0), dp.value)
                }
            }
            println("${currIdx.first} ${currIdx.second}")
        }

        printTerrain()


        val checked = mutableMapOf<Pair<Int, Int>, Int>().withDefault { 0 }
        val toCheck = mutableListOf<Pair<Int, Int>>()
        val firstFilledRow = terrain2.withIndex().firstNotNullOf { (idx, arr) ->
            if (arr.distinct().count() > 1) idx else null
        }
        terrain2[firstFilledRow].mapIndexedNotNull { idx, ter ->
            if (ter == Terrain.EMPTY) null else idx
        }.forEach { idx ->
            val newIdx = firstFilledRow + 1 to idx
            if (terrain2[firstFilledRow + 1][idx] == Terrain.EMPTY) {
                toCheck.add(newIdx)
                checked[newIdx] = 1
            }
        }
        val directions = listOf((-1 to 0), (0 to -1), (1 to 0), (0 to 1))
        while (toCheck.isNotEmpty()) {
            val first = toCheck.removeFirst()
            directions.map { pair -> first + pair }.forEach { idx ->
                if (idx.first in terrain2.indices && idx.second in terrain2[0].indices && terrain2[idx.first][idx.second] == Terrain.EMPTY) {
                    if (checked[idx] != 1) {
                        checked[idx] = 1
                        toCheck.add(idx)
                    }
                }
            }
        }
        return checked.values.sum() + terrain2.sumOf { arr ->
            arr.fold(0) { acc: Int, terrain ->
                acc + (terrain != Terrain.EMPTY).booleanToInt()
            }
        }
    }

    private fun Boolean.booleanToInt() = if (this) 1 else 0

    private fun digEast(start: Pair<Int, Int>, value: Int) = (1..value).forEach { i ->
        terrain2[start.first][start.second + i] = Terrain.H
    }

    private fun digSouth(start: Pair<Int, Int>, value: Int) = (1..value).forEach { i ->
        terrain2[start.first + i][start.second] = Terrain.V
    }

    private fun printTerrain() {
        terrain2.forEach { arr ->
            arr.forEach {
                print(it.value)
            }
            println()
        }
    }

    override fun part2(): Int {
        return 0
    }

    data class DigPlan(
            val dir: Dir,
            val value: Int,
            val hex: String,
    )

    enum class Terrain(val value: Char) {
        EMPTY('.'), H('#'), V('X'), VN('I')
    }

}