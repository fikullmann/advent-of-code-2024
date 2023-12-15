import java.io.File

fun main() {
    Day15.part1().let(::println)
    Day15.part2().let(::println)
    Day15.solve().let(::println)
}
object Day15: Day<Int, Int>() {
    val input = File("input/15").readLines()[0].split(',')

    private fun hash(str: String): Int = str.fold(0) { acc, ch ->
            (acc + ch.code) * 17 % 256
        }

    override fun part1(): Int {
        val results = mutableListOf<Int>()
        input.forEach { str ->
            results.add(hash(str))
        }
        return results.sum()
    }
    override fun part2() = input.fold(Array(256) { mutableMapOf<String, Int>() }) { acc, str ->
        val (id, focalLength) = str.split('=', '-')
        if (str.contains('-')) { acc[hash(id)] -= id }
        else { acc[hash(id)][id] = focalLength.toInt() }
        acc
    }.withIndex().sumOf { (idx, entry) ->
        entry.values.withIndex().sumOf { (slot, lens) ->
            (idx + 1) * (slot + 1) * lens
        }
    }
}
