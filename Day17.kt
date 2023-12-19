import java.io.File
import java.util.PriorityQueue
import kotlin.math.max
import kotlin.math.min

fun main() {
    Day17.part2().let(::println)
}

object Day17 : Day<Int, Int>() {
    /**
     * parsed input as 2d array of heat map layout
     */
    private val hlLayout: List<List<Int>> = File("input/17").readLines().map { str ->
        str.map { ch -> ch.digitToInt() }
    }

    private val costMap = mutableMapOf<HeatLoss, Int>().withDefault { Int.MAX_VALUE }
    private val unvisited = PriorityQueue<Cost>(compareBy { it.cost })

    override fun part1(): Int {
        listOf( HeatLoss(0 to 0, Dir.EAST, 0),
                HeatLoss(0 to 0, Dir.SOUTH, 0)
        ).forEach { hl ->
            unvisited.add(Cost(hl, 0))
            costMap[hl] = 0
        }
        val endIndex = (hlLayout.size - 1) to (hlLayout[0].size - 1)
        //println(endIndex.first * endIndex.second)
        var i = 0
        while (unvisited.isNotEmpty()) {
            if (i % 1000 == 0) println(i)
            i++
            val currNode = unvisited.poll()
            if (currNode.hl.idx == endIndex) {
                return currNode.cost
            }
            if (costMap[currNode.hl] == currNode.cost) {
                step(currNode, 0, 3)
            }
        }
        return 0
    }

    override fun part2(): Int {

        listOf( HeatLoss(0 to 0, Dir.EAST, 0),
                HeatLoss(0 to 0, Dir.SOUTH, 0)
        ).forEach { hl ->
            unvisited.add(Cost(hl, 0))
            costMap[hl] = 0
        }
        val endIndex = (hlLayout.size - 1) to (hlLayout[0].size - 1)
        //println(endIndex.first * endIndex.second)
        var i = 0
        while (unvisited.isNotEmpty()) {
            if (i % 1000 == 0) println(i)
            i++
            val currNode = unvisited.poll()
            if (currNode.hl.idx == endIndex) {
                return currNode.cost
            }
            if (costMap[currNode.hl] == currNode.cost) {
                step(currNode, 4, 10)
            }
        }
        return 0
    }

    private fun step(c: Cost, min: Int, max: Int) {
        c.hl.nextSteps(min, max).forEach {hl ->
            val existingCost = costMap[hl] ?: Int.MAX_VALUE // if already visited we don't need to visit again
            val cost = c.cost + calculateCost(c.hl, hl)!!
            if (existingCost > cost) {
                costMap[hl] = cost
                unvisited.add(Cost(hl, cost))
            }
        }
        // for each direction -> find other eligble directions
        // add next nodes to unvisited
    }

    fun calculateCost(hlFrom: HeatLoss, hlTo: HeatLoss): Int? {
        // if lIdx equal -> iterate over colums
        if (hlFrom.idx.first == hlTo.idx.first) {
            val min = min(hlTo.idx.second, hlFrom.idx.second)
            val max = max(hlTo.idx.second, hlFrom.idx.second)
            return (min..max).fold(0) { acc, idx ->
                acc + hlLayout[hlFrom.idx.first][idx]
            } - hlLayout[hlFrom.idx.first][hlFrom.idx.second]
        } else if (hlFrom.idx.second == hlTo.idx.second) { // if cIdx equal -> iterate over lines
            val min = min(hlTo.idx.first, hlFrom.idx.first)
            val max = max(hlTo.idx.first, hlFrom.idx.first)
            return (min..max).fold(0) { acc, idx ->
                acc + hlLayout[idx][hlFrom.idx.second]
            } - hlLayout[hlFrom.idx.first][hlFrom.idx.second]
        }
        return null
    }

    private fun validIdx(idx: Pair<Int, Int>): Boolean =
            idx.first in hlLayout.indices && idx.second in hlLayout.first().indices

    private fun mapDir2(idx: Pair<Int, Int>, min: Int, dir: Dir) {
        var distance = 0
        for (i in 1..3) {
            var resultIdx = when (dir) {
                Dir.NORTH -> (-i to 0)
                Dir.WEST -> (0 to -i)
                Dir.SOUTH -> (i to 0)
                Dir.EAST -> (0 to i)
            }
            resultIdx = (resultIdx.first + idx.first) to (resultIdx.second + idx.second)
            if (resultIdx.first in hlLayout.indices && resultIdx.second in hlLayout[0].indices) {
                distance += hlLayout[resultIdx.first][resultIdx.second]
            }
        }
        for (i in 4..10) {
            var resultIdx = when (dir) {
                Dir.NORTH -> (-i to 0)
                Dir.WEST -> (0 to -i)
                Dir.SOUTH -> (i to 0)
                Dir.EAST -> (0 to i)
            }
            resultIdx = (resultIdx.first + idx.first) to (resultIdx.second + idx.second)
            if (resultIdx.first in hlLayout.indices && resultIdx.second in hlLayout[0].indices) {
                distance += hlLayout[resultIdx.first][resultIdx.second]
                val newHL = HeatLoss(resultIdx, dir, i)
                val existingCost = costMap[newHL] ?: Int.MAX_VALUE// if already visited we don't need to visit again
                if (existingCost > distance + min) {
                    costMap[newHL] = distance + min
                    unvisited.add(Cost(newHL, distance + min))
                }
            }
        }
    }

    data class Cost(
            val hl: HeatLoss,
            val cost: Int
    )

    data class HeatLoss(
            val idx: Pair<Int, Int>,
            val dir: Dir,
            val steps: Int
    ) {
        fun nextSteps(minBlock: Int = 1, maxBlock: Int = 3) = buildList<HeatLoss> {
            when (dir) {
                Dir.NORTH, Dir.SOUTH -> {
                    // return listOf 3 west heatlosses + 3 east heatlosses
                    for (i in minBlock..maxBlock) {
                        add(copy(idx = idx + i * (0 to -1), dir = Dir.WEST, steps = i))
                        add(copy(idx = idx + i * (0 to 1), dir = Dir.EAST, steps = i))
                    }
                }

                Dir.EAST, Dir.WEST -> {
                    for (i in minBlock..maxBlock) {
                        add(copy(idx = idx + i * (-1 to 0), dir = Dir.NORTH, steps = i))
                        add(copy(idx = idx + i * (1 to 0), dir = Dir.SOUTH, steps = i))
                    }
                }
            }
        }.filter {
            it.idx.first in hlLayout.indices && it.idx.second in hlLayout.first().indices
        }
    }
}
