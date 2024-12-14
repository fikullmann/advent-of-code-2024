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
enum class Direction(row: Int, col: Int) {
    UP(-1,0), DOWN(1, 0), LEFT(0, -1), RIGHT(0, 1);

    fun ninetyDegree(): Direction {
        when (this) {
            UP -> return RIGHT
            DOWN -> return LEFT
            LEFT -> return UP
            RIGHT -> return DOWN
        }
    }

    fun applyTo(loc: Location): Location {
        when (this@Direction) {
            UP -> return loc.up()
            DOWN -> return loc.down()
            LEFT -> return loc.left()
            RIGHT -> return loc.right()
        }
    }
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
    fun left() = Location(row, col - 1)
    fun right() = Location(row, col + 1)
    fun up() = Location(row - 1, col)
    fun down() = Location(row + 1, col)


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
fun Long.lengthInt() = when(this) {
    0L -> 1
    else -> log10(abs(toDouble())).toInt() + 1
}