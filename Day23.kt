import java.io.File
import java.util.PriorityQueue
import kotlin.math.max

fun main() {
    Day23.solve().let(::println)
}

object Day23 : Day<Int, Int>() {
    val garden: MutableMap<Pair<Int, Int>, Trails> = mutableMapOf()
    var cSize = 0
    var lSize = 0

    // for part2
    val idxCrossing: MutableMap<Pair<Int, Int>, Crossing> = mutableMapOf()

    init {
        File("input/23").readLines().forEachIndexed() { i, str ->
            str.forEachIndexed() { j, ch ->
                if (ch == 'S') {
                    garden[i to j] = Trails.fromChar('.')
                } else {
                    garden[i to j] = Trails.fromChar(ch)
                }
            }
        }
        lSize = garden.keys.maxOf { it.first }
        cSize = garden.keys.maxOf { it.second }
    }
    private val endIdx = lSize to cSize - 1

    override fun part1(): Int {
        val workList: MutableList<Step> = mutableListOf(Step(0 to 1, Dir.NORTH, 0))
        var max = 0
        while (workList.isNotEmpty()) {
            val curr = workList.removeFirst()
            if (curr.idx == endIdx) {
                max = max(max, curr.distance)
            } else {
                curr.nextSteps().forEach {
                    workList.add(it)
                }
            }
        }
        return max
    }

    override fun part2(): Int {

        idxCrossing[0 to 1] = Crossing(0 to 1)
        idxCrossing[endIdx] = Crossing(endIdx)
        val workList: MutableList<Pair<Crossing, Dir>> = mutableListOf(
                idxCrossing[0 to 1]!! to Dir.NORTH,
                idxCrossing[endIdx]!! to Dir.SOUTH,
        )

        // build condensed graph of Crossings
        while (workList.isNotEmpty()) {
            val (curr, dir) = workList.removeFirst()
            val crossList = Step(curr.idx, dir, 0).nextStepsIgnoreSteeps().toMutableList()
            while (crossList.isNotEmpty()) {
                val currPath = crossList.removeFirst()
                if (currPath.nextStepsIgnoreSteeps().size > 1) {
                    if (!idxCrossing.containsKey(currPath.idx)) {
                        val newCrossing = curr.connectNew(currPath.idx, currPath.distance)
                        idxCrossing[currPath.idx] = newCrossing
                        workList.add(newCrossing to currPath.dir)
                    } else {
                        curr.connect(idxCrossing[currPath.idx]!!, currPath.distance)
                    }
                } else {
                    currPath.nextStepsIgnoreSteeps().firstOrNull()?.let { crossList.add(0, it) }
                }
            }
        }

        // now: dijkstra
        val longestPathList: PriorityQueue<Step> = PriorityQueue(compareByDescending { it.distance })
        longestPathList.add(Step(0 to 1, Dir.NORTH, 0))
        var max = 0
        while (longestPathList.isNotEmpty()) {
            val curr = longestPathList.poll()
            if (curr.idx == endIdx) {
                max = max(max, curr.distance)
                //println(max)
            } else {
                curr.nextNode().forEach {
                    longestPathList.add(it)
                }
            }
        }
        return max
    }
    data class Crossing(
            val idx: Pair<Int, Int>,
    ) {
        val connections: MutableList<Pair<Crossing, Int>> = mutableListOf()
        fun connectNew(o: Pair<Int, Int>, dist: Int): Crossing {
            val new = Crossing(o)
            this.connections.add(new to dist)
            new.connections.add(this to dist)
            return new
        }
        fun connect(crossing: Crossing, dist: Int) {
            if (connections.find { it.first == crossing } == null) {
                this.connections.add(crossing to dist)
                crossing.connections.add(this to dist)
            }
        }
    }


    data class Step(
            val idx: Pair<Int, Int>,
            val dir: Dir,
            val distance: Int = 0,
            private val visited: MutableMap<Pair<Int, Int>, Int> = mutableMapOf<Pair<Int, Int>, Int>().withDefault { 0 },
    ) {

        fun nextSteps(): List<Step> = buildList {
            val dist = distance + 1
            val vis = visited.toMutableMap()
            vis[idx] = 1
            when (garden[idx]) {
                Trails.FOREST -> Unit
                Trails.PATH -> {
                    add(Step(idx + (0 to 1), Dir.WEST, dist, vis))
                    add(Step(idx + (0 to -1), Dir.EAST, dist, vis))
                    add(Step(idx + (1 to 0), Dir.NORTH, dist, vis))
                    add(Step(idx + (-1 to 0), Dir.SOUTH, dist, vis))
                }

                Trails.NSLOPE -> add(Step(idx + (-1 to 0), Dir.SOUTH, dist, vis))
                Trails.SSLOPE -> add(Step(idx + (1 to 0), Dir.NORTH, dist, vis))
                Trails.WSLOPE -> add(Step(idx + (0 to -1), Dir.EAST, dist, vis))
                Trails.ESLOPE -> add(Step(idx + (0 to 1), Dir.WEST, dist, vis))
                null -> Unit
            }
        }.filter { newStep ->
            garden[newStep.idx] != Trails.FOREST && newStep.idx.first in 0..lSize && newStep.idx.second in 0..cSize
                    && visited[newStep.idx] != 1
        }

        fun nextStepsIgnoreSteeps(): List<Step> = buildList {
            val dist = distance +1
            //val vis = visited.toMutableMap()
            //visited[idx] = 1
            when (garden[idx]) {
                Trails.FOREST -> Unit
                else -> {
                    if (dir != Dir.EAST) add(Step(idx = idx + (0 to 1), dir = Dir.WEST, distance = dist))
                    if (dir != Dir.WEST) add(Step(idx = idx + (0 to -1), dir = Dir.EAST, distance = dist))
                    if (dir != Dir.SOUTH) add(Step(idx = idx + (1 to 0), dir = Dir.NORTH, distance = dist))
                    if (dir != Dir.NORTH) add(Step(idx = idx + (-1 to 0), dir = Dir.SOUTH, distance = dist))
                }
            }
        }.filter { newStep ->
            garden[newStep.idx] != Trails.FOREST && newStep.idx.first in 0..lSize && newStep.idx.second in 0..cSize
        }

        fun nextNode(): List<Step> = buildList {
            val currCrossing = idxCrossing[idx]!!
            val vis = visited.toMutableMap()
            vis[idx] = 1
            currCrossing.connections.forEach { (crossing, dist) ->
                if (!vis.contains(crossing.idx)) {
                    add(Step(crossing.idx, Dir.NORTH, distance + dist, vis))
                }
            }
        }
    }


    enum class Trails(val value: Char) {
        PATH('.'), FOREST('#'),
        NSLOPE('^'), SSLOPE('v'), WSLOPE('<'), ESLOPE('>');

        companion object {
            fun fromChar(value: Char) = entries.first { it.value == value }
        }
    }
}