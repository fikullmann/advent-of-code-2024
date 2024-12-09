import java.io.File
import kotlin.math.abs
import kotlin.math.log10

fun main() {
    //Day9.part1().let (::println)
    Day9.part2().let (::println)
}

object Day9 : Day<Long, Long>() {
    private val input = File("input/9").readLines().first().map { it.digitToInt() }
    var list: MutableList<Int?> = mutableListOf()

    override fun part1(): Long {
        var runningDiskId = 0
        for (i in input.indices step 2) {
            repeat(input[i]) {
                list.add(runningDiskId)
            }
            if (i+1 < input.size) {
                repeat(input[i + 1]) {
                    list.add(null)
                }
            }
            runningDiskId++
        }
        for (i in list.indices) {
            if (i >= list.size) break
            if (list[i] == null) {
                while (list.last() == null) { list.removeLast() }
                if (i >= list.size) break
                list[i] = list.removeLast()
            }
        }

        return list.indices.sumOf { i ->
            (i * list[i]!!).toLong()
        }
    }

    override fun part2(): Long {
        class Disk(val idx: Int, val fileId: Int, val length: Int, var freeSpace: Int) {
            fun freeIdx() = (idx + length)
        }
        val disks: MutableList<Disk> = mutableListOf()
        var runningDiskId = 0
        var runningIdx = 0

        for (i in input.indices step 2) {
            disks.add(Disk(runningIdx, runningDiskId, input[i], (if (i+1 < input.size) input[i+1] else 0)))
            runningIdx += (input[i] + (if (i+1 < input.size) input[i+1] else 0))
            runningDiskId++
        }

        var i = disks.size - 1
        while (i >= 0) {
            val disc = disks[i]
            val fittingIdx = disks.indexOfFirst { it.freeSpace >= disc.length && it.idx < disc.idx } // find index of fitting space
            if (fittingIdx >= 0) {
                if (i-1>= 0) disks[i - 1].freeSpace += (disc.length + disc.freeSpace) // add our length to free space of previous disk
                disks.removeAt(i)
                disks.add( // insert disc
                    fittingIdx + 1,
                    Disk(
                        disks[fittingIdx].freeIdx(),
                        disc.fileId,
                        disc.length,
                        disks[fittingIdx].freeSpace - disc.length
                    )
                )
                disks[fittingIdx].freeSpace = 0 // adjust previous disk where input
            } else i--
        }

        i = 0
        var sum: Long = 0
        disks.forEach { disc ->
            repeat(disc.length) {
                sum += (disc.fileId * i).toLong()
                i++
            }
            i+= disc.freeSpace
        }
        return sum
    }
    fun Int.length() = when(this) {
        0 -> 1
        else -> log10(abs(toDouble())).toInt() + 1
    }
}
