operator fun Pair<Int, Int>.plus(o: Pair<Int, Int>) = (this.first + o.first) to (this.second + o.second)
operator fun Pair<Int, Int>.minus(o: Pair<Int, Int>) = (this.first + o.first) to (this.second + o.second)
operator fun Pair<Int, Int>.times(o: Int) = o *first to o * second
operator fun Int.times(o: Pair<Int, Int>) = this *o.first to this * o.second

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