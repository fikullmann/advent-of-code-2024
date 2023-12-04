import java.io.File
import kotlin.math.ceil

fun main() {
    day6Part2(File("input/6"))
}

fun day6Part1(content: File) {
    val times = content.readLines()[0].split(Regex(" +")).drop(1).mapNotNull { it.toIntOrNull() }
    val distances = content.readLines()[1].split(Regex(" +")).drop(1).mapNotNull { it.toIntOrNull() }
    val races: MutableList<Race> = mutableListOf()
    for (i in times.indices) {
        races.add(Race(times[i], distances[i]))
    }
    val numDurations: MutableList<Int> = mutableListOf()
    races.forEach { race ->
        val startIdx = ceil(race.distance.toDouble() / race.time).toInt()
        for (i in startIdx..<race.time) {
            if (i * (race.time - i) > race.distance) {
                numDurations.add(race.time - (2 * i) + 1)
                break
            }
        }
    }
    var value = 1
    numDurations.forEach {
        value *= it
    }
    println(value)
}

fun day6Part2(content: File) {
    val time = content.readLines()[0].split(Regex(" +")).drop(1).reversed().foldRight("") { time, acc ->
        acc + time
    }.toLong()
    val distance = content.readLines()[1].split(Regex(" +")).drop(1).reversed().foldRight("") { distance, acc ->
        acc + distance
    }.toLong()
    val startIdx = ceil(distance.toDouble() / time).toLong()
    for (i in startIdx..<time) {
        if (i * (time - i) > distance) {
            println(time - (2 * i) + 1)
            break
        }
    }
}

class Race(val time: Int, val distance: Int)