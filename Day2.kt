import java.io.File

fun main() {
    //Day2.part1().let (::println)
    Day2.part2().let (::println)
    //Day2.solve().let(::println)
}

object Day2 : Day<Int, Int>() {
    private val reports = File("input/2").readLines().map { it.split(" ").map { it.toInt() } }

    override fun part1(): Int {
        var safeSum = 0
        reports.forEach { levels ->
            var increasing: Boolean = ((levels[1] - levels[0]) > 0).boolToInt() + ((levels[2] - levels[1]) > 0).boolToInt() + ((levels[3] - levels[2]) > 0).boolToInt() > 2
            var correct = true
            for (i in 0..<levels.size -1) {
                val diff = levels[i+1] - levels[i]
                if (diff > 0 && increasing && diff <= 3) {
                } else if (diff < 0 && !increasing && diff >= -3) {
                } else {
                    correct = false
                    break
                }
            }
            safeSum += if (correct) 1 else 0
        }
        return safeSum
    }

    override fun part2(): Int {
        // check first difference and last difference to get correct increasing
        var safeSum = 0
        reports.forEach { levels ->
            val increasing: Boolean = ((levels[1] - levels[0]) > 0).boolToInt() + ((levels[2] - levels[1]) > 0).boolToInt() + ((levels[3] - levels[2]) > 0).boolToInt() >= 2
            var correct = false
            var idx = 0
            while (!correct && idx < levels.size) {
                val levelCopy = levels.toMutableList()
                levelCopy.removeAt(idx)
                correct = true
                for (i in 1..<levelCopy.size) {
                    val diff = levelCopy[i] - levelCopy[i-1]
                    if (diff > 0 && increasing && diff <= 3) {
                    } else if (diff < 0 && !increasing && diff >= -3) {
                    } else {
                        correct = false
                        break
                    }
                }
                idx++
            }
            safeSum += correct.boolToInt()
        }
        return safeSum
    }

    fun Boolean.boolToInt(): Int = if (this) 1 else 0
}
