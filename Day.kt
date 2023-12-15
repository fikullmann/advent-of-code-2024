import kotlin.time.TimedValue
import kotlin.time.measureTimedValue

abstract class Day<T, V>() {
    abstract fun part1(): T
    abstract fun part2(): V

    fun solve(): TimedValue<Pair<T, V>> {
        return measureTimedValue { part1() to part2() }
    }
}