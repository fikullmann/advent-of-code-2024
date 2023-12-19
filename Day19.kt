import java.io.File
import kotlin.math.max
import kotlin.math.min

fun main() {
    Day19.part2().let(::println)
}

object Day19 : Day<Long, Long>() {
    private val workflows: MutableMap<String, List<Pair<String, String>>> = mutableMapOf()
    private val xmas: MutableList<XMASPart> = mutableListOf()
    private val input = File("input/19").readLines().forEach { str ->
        if (str.isNotEmpty()) {
            if (str.first() == '{') {
                val (x, m, a, s) = str.drop(1).dropLast(1).split(',')
                xmas.add(XMASPart(x.drop(2).toInt(), m.drop(2).toInt(), a.drop(2).toInt(), s.drop(2).toInt()))
            } else {
                val lineList = str.dropLast(1).split('{', ',')
                val name = lineList.first()

                workflows[name] = lineList.drop(1).map {
                    val colonSplit = it.split(':')
                    if (colonSplit.size > 1) colonSplit[0] to colonSplit[1]
                    else "" to colonSplit[0]
                }.toMutableList()
            }
        }
    }


    override fun part1(): Long = xmas.fold(0L) { acc, xmasPart ->
        var value = 0L
        var workList: MutableList<Pair<String, String>> = workflows["in"]?.toMutableList()!!
        while (workList.isNotEmpty()) {
            val curr = workList.removeFirst()
            if (xmasPart.calculateDestination(curr.first)) {
                val dest = curr.second
                when (dest) {
                    "A" -> {
                        value = xmasPart.value(); break
                    }

                    "R" -> break
                    else -> {
                        workList = workflows[curr.second]?.toMutableList()!!
                    }
                }
            }
        }
        acc + value
        // take first value of in, calc Desination
        // if true -> check for second value, if A or R finish, if other search in workflow and go in next iteration
    }

    override fun part2(): Long {
        // start with in
        // create node for in with worklist and valid ranges
        // oder x: 0 to 4000 etc.
        // for each part:
        //
        val nodes = mutableListOf(XMASNode("in", 1 to 4000, 1 to 4000, 1 to 4000, 1 to 4000))
        var value = 0L
        while (nodes.isNotEmpty()) {
            val curr = nodes.removeFirst()
            when (curr.name) {
                "A" -> value += curr.value()
                "R" -> value += 0L
                else -> nodes.addAll(curr.createNext())
            }
        }

        return value
    }

    data class XMASNode(
            val name: String,
            val x: Pair<Int, Int>,
            val m: Pair<Int, Int>,
            val a: Pair<Int, Int>,
            val s: Pair<Int, Int>,
    ) {
        fun createNext(): List<XMASNode> = buildList {
            val workList = workflows[name]?.toMutableList()!!
            var xNew = x
            var mNew = m
            var aNew = a
            var sNew = s
            while (workList.isNotEmpty()) {
                //"s < 1351"
                //"a > 1761"
                // create object where everything is the same but second of s = 1350
                // continue with configuration where s.first = 1351 and do same thing for next limit is corrected
                val curr = workList.removeFirst()
                var xNext = xNew
                var mNext = mNew
                var aNext = aNew
                var sNext = sNew
                if (curr.first.isNotEmpty()) {
                    val value = curr.first.drop(2).toInt()
                    var first: Int? = null
                    var second: Int? = null
                    when (curr.first[1]) {
                        '>' -> first = value
                        '<' -> second = value
                    }
                    when (curr.first[0]) {
                        'x' -> {
                            //xNext = first?.let { it+1 to xNew.second } ?: second?.let { xNew.first to it-1 }!!
                            xNext = max(xNew.first, (first ?: -1)+1) to min(xNew.second, (second ?: 4001)-1)
                            xNew = max(xNew.first, second ?: 0) to min(xNew.second, first ?: 4000)
                        }
                        'm' -> {

                            mNext = max(mNew.first,  (first ?: -1)+1) to min(mNew.second, (second ?: 4001)-1)
                            mNew = max(mNew.first, second ?: 0) to min(mNew.second, first ?: 4000)
                        }
                        'a' -> {
                            aNext = max(aNew.first,  (first ?: -1)+1) to min(aNew.second, (second ?: 4001)-1)
                            aNew = max(aNew.first, second ?: 0) to min(aNew.second, first ?: 4000)
                        }
                        's' -> {
                            sNext = max(sNew.first,  (first ?: -1)+1) to min(sNew.second, (second ?: 4001)-1)
                            sNew = max(sNew.first, second ?: 0) to min(sNew.second, first ?: 4000)
                        }
                    }
                    add(XMASNode(curr.second, xNext, mNext, aNext, sNext))
                } else {
                    add(copy(name = curr.second, x = xNew, m = mNew, a=aNew, s=sNew))
                }
            }
            // find restriction & create new node with goal
            // for every succeeding part, take opposite of previous restriction as additional restriction
        }

        fun value(): Long = (x.size() * m.size() * a.size() * s.size())
        private fun Pair<Int, Int>.size() = ((this.second - first) + 1).toLong()
    }

    data class XMASPart(val x: Int, val m: Int, val a: Int, val s: Int) {
        fun calculateDestination(rating: String): Boolean {
            if (rating.isEmpty()) return true
            var toCheck = 0
            val value = rating.drop(2).toInt()
            when (rating[0]) {
                'x' -> toCheck = x
                'm' -> toCheck = m
                'a' -> toCheck = a
                's' -> toCheck = s
            }
            return when (rating[1]) {
                '>' -> toCheck > value
                '<' -> toCheck < value
                else -> false
            }
        }

        fun value(): Long = (x + m + a + s).toLong()
    }
}