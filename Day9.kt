import java.io.File

fun main() {
    day9Part2(File("input/9"))
}

fun day9Part1(content: File) {
    val sequences: MutableList<NumSeq> = mutableListOf()
    content.forEachLine { str ->
        sequences.add(NumSeq(str.split(Regex(" +")).map { it.toLong() }))
    }
    val increases = sequences.map { numseq ->
        val process: MutableList<Long> = mutableListOf(numseq.values.last())
        var seq = numseq
        var allSame = false
        while (!allSame) {
            seq = NumSeq(seq.diff())
            process.add(seq.values.last())
            if (seq.finish()) {
                allSame = true
            }
        }
        process.reverse()
        val increase = process.fold(0) { acc: Long, i ->
            acc + i
        }
        increase
    }
    println(increases.sum())
}

class NumSeq(val values: List<Long>) {
    fun diff(): List<Long> {
        return List(values.size - 1) { i ->
            values[i + 1] - values[i]
        }
    }

    fun finish(): Boolean = values.distinct().count() == 1
}

fun day9Part2(content: File) {
    val sequences: MutableList<NumSeq> = mutableListOf()
    content.forEachLine { str ->
        sequences.add(NumSeq(str.split(Regex(" +")).map { it.toLong() }))
    }
    val increases = sequences.map { numseq ->
        val process: MutableList<Long> = mutableListOf(numseq.values.first())
        var seq = numseq
        var allSame = false
        while (!allSame) {
            seq = NumSeq(seq.diff())
            process.add(seq.values.first())
            if (seq.finish()) {
                allSame = true
            }
        }
        process.reverse()
        val increase = process.reduce { acc, l ->
            l - acc
        }
        increase
    }
    println(increases.sum())
}