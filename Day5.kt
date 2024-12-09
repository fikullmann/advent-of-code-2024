import java.io.File

fun main() {
    //Day5.part1().let (::println)
    Day5.part2().let (::println)
}

object Day5 : Day<Int, Int>() {
    private val input = File("input/5").readLines()
    var idx = 0
    val rules: Map<Int, MutableList<Int>> = buildMap<Int, MutableList<Int>> {
        while (input[idx].isNotEmpty()) {
            val (fst, snd) = input[idx].split("|").map { it.toInt() }
            this.getOrPut(fst, ::mutableListOf ).add(snd)
            idx++
        }
    }
    val sequences: List<List<Int>> = buildList() {
        idx++
        while (idx < input.size) {
            add(input[idx].split(",").map { it.toInt() })
            idx++
        }
    }

    override fun part1(): Int {
        return sequences.sumOf { seq ->
            var valid = true
            for (i in 0..<seq.size -1) {
                valid = valid && rules[seq[i]]?.containsAll(seq.drop(i+1)) == true
            }
            if (valid) seq[seq.size / 2] else 0
        }
    }

    override fun part2(): Int {
        return sequences.sumOf { seq ->
            var valid = true
            for (i in 0..<seq.size -1) {
                valid = valid && rules[seq[i]]?.containsAll(seq.drop(i+1)) == true
            }
            if (!valid) {
                val newSeq = order(seq)
                newSeq[newSeq.size / 2]
            } else 0
        }
    }

    fun order(seq: List<Int>, resultSeq: List<Int> = listOf()): List<Int> {
        if (seq.size == 1) return resultSeq + seq.first()
        var resultList: List<Int> = listOf()
        seq.forEachIndexed { i, number ->
            if (rules[number]?.containsAll(seq.minus(number)) == true) {
                order(seq.minus(number), resultSeq + number).let {
                    if (it.isNotEmpty()) resultList = it
                }
            }
        }
        return if (resultList.isEmpty()) listOf() else resultList
    }
}
