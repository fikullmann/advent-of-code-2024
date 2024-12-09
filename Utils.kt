import kotlin.math.abs
import kotlin.math.log10

operator fun Pair<Int, Int>.plus(o: Pair<Int, Int>) = (this.first + o.first) to (this.second + o.second)
operator fun Pair<Int, Int>.minus(o: Pair<Int, Int>) = (this.first + o.first) to (this.second + o.second)
operator fun Pair<Int, Int>.times(o: Int) = o *first to o * second
operator fun Int.times(o: Pair<Int, Int>) = this *o.first to this * o.second

fun distance(i:Int, j: Int) = abs(i - j)

enum class Dir {
    NORTH, SOUTH, EAST, WEST
}

// this part is from stackoverflow OWO help
private fun gcd(a: Long, b: Long): Long {
    var a = a
    var b = b
    while (b > 0) {
        val temp = b
        b = a % b // % is remainder
        a = temp
    }
    return a
}
fun gcd(input: List<Long>): Long {
    var result = input[0]
    for (i in 1 until input.size) result = gcd(result, input[i])
    return result
}

fun lcm(a: Long, b: Long): Long {
    return a * (b / gcd(a, b))
}

fun lcm(input: List<Long>): Long {
    var result = input[0]
    for (i in 1 until input.size) result = lcm(result, input[i])
    return result
}
data class Location(val row: Int, val col: Int) {
    fun distance(other: Location): Distance = Distance(row - other.row, col - other.col)

    fun applyDistance(other: Distance, n: Int = 1): Location =
        Location(row = row + other.row * n, col = col + other.col * n)
    fun applyDistance(other: Pair<Int, Int>, n: Int = 1): Location =
        Location(row = row + other.first * n, col = col + other.second * n)
    fun isInside(maxRow: Int, maxCol: Int): Boolean = row in 0..<maxRow && col in 0..<maxCol
}

fun Long.length() = when(this) {
    0L -> 1L
    else -> log10(abs(toDouble())).toLong() + 1
}