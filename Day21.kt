import java.io.File
import kotlin.math.pow

fun main() {
    Day21.part1().let(::println)
    Day21.part2().let(::println)
}

object Day21 : Day<Int, Long>() {

    val workList: MutableSet<Step> = mutableSetOf()
    val garden: MutableMap<Pair<Int, Int>, Garden> = mutableMapOf()
    var start = Step(0, 0 to 0)

    init {
        File("input/21").readLines().mapIndexed { i, str ->
            str.forEachIndexed() { j, ch ->
                if (ch == 'S') {
                    start = Step(0, i to j)
                    garden[i to j] = Garden.fromChar('.')
                } else {
                    garden[i to j] = Garden.fromChar(ch)
                }
            }
        }
    }


    override fun part1(): Int {
        return gardenWalk(64)
        //println(calculateX())
        //printGardenSteps()
    }

    private fun gardenWalk(step: Int): Int {
        workList.clear()
        workList.add(start)
        for (i in 0..<step) {
            if (workList.isEmpty()) break
            val iDistance = workList.filter { it.distance == i }
            workList.removeAll { it.distance == i }
            for (curr in iDistance) {
                workList.addAll(directions(curr.idx).filter { garden[it] == Garden.GARDEN_PLOT }.map {
                    Step(i+1, it)
                })
            }
        }
        return workList.size
    }

    fun printGardenSteps() {
        for (i in 0..<131) {
            for (j in 0..<131) {
                if (workList.filter { it.idx.first==i && it.idx.second==j }.isNotEmpty()) {
                    print('O')
                } else {
                    print(garden[i to j]?.value)
                }
            }
            println()
        }
    }

    override fun part2(): Long {
        val s = ((26501365.0 - 65) / 131)

        /*
        val x1 = gardenWalk(65).toLong()
        val x2 = gardenWalk(64).toLong()
        val y = ((gardenWalk(130) - x2 + gardenWalk(131) - x1).toDouble()/2).toLong()
        val result = (s + 1).pow(2.0).toLong()*x1 + s.pow(2.0).toLong()*x2 + ((1 + 2 * s).pow(2.0) /2).toLong() * y
        return result
        */
        val x1 = gardenWalk(131).toLong()
        val x2 = gardenWalk(130).toLong()
        val y1 = x1 - gardenWalk(65)
        val y2 = x2 - gardenWalk(64)

        return (s+1).pow(2).toLong()*x1 + s.pow(2).toLong()*x2 + (- (s+1) * y1 + s*y2).toLong()

    }

    private fun directions(pair : Pair<Int, Int>): List<Pair<Int, Int>> = buildList {
        add(pair + (0 to 1))
        add(pair + (0 to -1))
        add(pair + (1 to 0))
        add(pair + (-1 to 0))
    }

    data class Step(val distance: Int = 0,
                    val idx: Pair<Int, Int>
    )
    enum class Garden(val value: Char) {
        Rock('#'), GARDEN_PLOT('.');

        companion object {
            fun fromChar(value: Char) = entries.first { it.value == value }
        }
    }

}