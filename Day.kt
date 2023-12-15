import kotlin.time.measureTimedValue

abstract class Day() {
    abstract fun part1(): Any?
    abstract fun part2(): Any?

    fun solve(): Any? {
        return measureTimedValue { listOf(part1(), part2()) }
    }
}