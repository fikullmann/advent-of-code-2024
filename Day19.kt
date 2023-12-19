import java.io.File

fun main() {
    Day19.solve().let(::println)
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
        // take first value of in
        // iterate: calcDestination
        // if true -> check for second value, if A or R finish
        // if other -> search in workflow and go in next iteration
        var value = 0L
        var workList: MutableList<Pair<String, String>> = workflows["in"]?.toMutableList()!!
        while (workList.isNotEmpty()) {
            val curr = workList.removeFirst()
            if (xmasPart.calculateDestination(curr.first)) {
                val dest = curr.second
                when (dest) {
                    "A" -> { value = xmasPart.value(); break }
                    "R" -> break
                    else -> { workList = workflows[curr.second]?.toMutableList()!! }
                }
            }
        }
        acc + value
    }

    override fun part2(): Long {
        // start with in
        // create node for "in" with worklist and valid ranges
        // x = 1 to 4000 etc. (inclusive ranges)
        // let node create next nodes
        // iterate until find A or R
        val nodes = mutableListOf(XMASNode("in", 1 to 4000, 1 to 4000, 1 to 4000, 1 to 4000))
        var value = 0L
        while (nodes.isNotEmpty()) {
            val curr = nodes.removeFirst()
            when (curr.name) {
                "A" -> value += curr.combinations()
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
                // find restriction & create new node with goal
                // for every succeeding part, take opposite of previous restriction as additional restriction
                // for s < 1351:
                // create object where everything is the same but second of s = 1350
                // continue with configuration where s.first = 1351 and do same thing for next limit is corrected
                val curr = workList.removeFirst()
                var xNext = xNew
                var mNext = mNew
                var aNext = aNew
                var sNext = sNew
                if (curr.first.isNotEmpty()) {
                    val value = curr.first.drop(2).toInt()
                    when (curr.first[1]) {
                        '>' -> {
                            when (curr.first[0]) {
                                'x' -> { xNext = value + 1 to xNew.second; xNew = xNew.first to value }
                                'm' -> { mNext = value + 1 to mNew.second; mNew = mNew.first to value }
                                'a' -> { aNext = value + 1 to aNew.second; aNew = aNew.first to value }
                                's' -> { sNext = value + 1 to sNew.second; sNew = sNew.first to value }
                            }
                        }
                        '<' -> {
                            when (curr.first[0]) {
                                'x' -> { xNext = xNew.first to value -1; xNew = value to xNew.second }
                                'm' -> { mNext = mNew.first to value -1; mNew = value to mNew.second }
                                'a' -> { aNext = aNew.first to value -1; aNew = value to aNew.second }
                                's' -> { sNext = sNew.first to value -1; sNew = value to sNew.second }
                            }
                        }
                    }
                    add(XMASNode(curr.second, xNext, mNext, aNext, sNext))
                } else {
                    add(copy(name = curr.second, x = xNew, m = mNew, a=aNew, s=sNew))
                }
            }
        }

        fun combinations(): Long = (x.size() * m.size() * a.size() * s.size())
        private fun Pair<Int, Int>.size() = ((this.second - first) + 1).toLong()
    }

    data class XMASPart(val x: Int, val m: Int, val a: Int, val s: Int) {
        /**
         * for a given rating such as: "s < 1351" returns whether the current XMAS part adheres to the rule
         * if there's no rule: "", returns true
         */
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