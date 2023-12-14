import java.io.File

fun main() {
    val lines = File("input/14").readLines()

    val input: MutableList<List<Roeck>> = mutableListOf()
    lines.forEach { line ->
        val newLine: MutableList<Roeck> = mutableListOf()
        line.forEach { ch ->
            if (ch == 'O') {
                newLine.add(Roeck.ROUND)
            } else if (ch == '#') {
                newLine.add(Roeck.CS)
            } else {
                newLine.add(Roeck.EMPTY)
            }
        }
        input.add(newLine)
    }
    //printPattern(input)
    day14Part2(input)
}

enum class Roeck(val ch: Char) {
    ROUND('O'), CS('#'), EMPTY('.')
}

fun printPattern(input: List<List<Roeck>>) {
    input.forEach {line ->
        line.forEach {
            print(it.ch)
        }
        println()
    }
}

fun day14Part1(input: List<List<Roeck>>) {
    var result = tiltNorth(input) // north
    println("-------------------------------------------------")
    printPattern(result)
    println(calculateNorthLoad(result))
}

fun tiltNorth(input: List<List<Roeck>>): List<List<Roeck>> {
    var traversal: List<List<Roeck>> = input
    var result: MutableList<MutableList<Roeck>> = mutableListOf()
    val northList = IntArray(input[0].size) { 0 }
    traversal.forEachIndexed { lineIdx, line ->
        result.add(line.toMutableList())
        line.forEachIndexed { columnIdx, ch ->
            if (ch == Roeck.ROUND) {
                result[lineIdx][columnIdx] = Roeck.EMPTY
                result[northList[columnIdx]][columnIdx] = Roeck.ROUND
                northList[columnIdx]++
            } else if (ch == Roeck.CS) {
                northList[columnIdx] = lineIdx + 1
            }
        }
    }
    return result
}
fun tiltWest(input: List<List<Roeck>>): List<List<Roeck>> {
    var result: MutableList<MutableList<Roeck>> = mutableListOf<MutableList<Roeck>>().apply {
        repeat(input.size) {
            this.add(mutableListOf())
        }
    }
    input.forEachIndexed { idx, line ->
        line.forEachIndexed { cIdx, r ->
            result[cIdx].add(r)
        }
    }
    val traversal = tiltNorth(result)
    result = mutableListOf<MutableList<Roeck>>().apply {
        repeat(input.size) {
            this.add(mutableListOf())
        }
    }
    traversal.forEachIndexed { idx, line ->
        line.forEachIndexed { cIdx, r ->
            result[cIdx].add(r)
        }
    }
    return result
}

fun calculateNorthLoad(input: List<List<Roeck>>): Int {
    val se = input.size
    var sum = 0
    input.forEachIndexed { idx, line ->
        line.forEach {r ->
            if (r == Roeck.ROUND) {
                sum += se - idx
            }
        }
    }
    return sum
}

fun day14Part2(input: List<List<Roeck>>) {
    var result = input
    var cycleLength = -1
    val map: HashMap<String, Int> = hashMapOf() // string to index
    var i = 0
    var x = 1000000000
    //println("brute force: " + cycleXTimes(input, x))
    while(cycleLength < 0 && i < 150) {
        result = oneCycle(result)
        val stringed = result.toString()
        if (map.containsKey(stringed)) {
            cycleLength = i - map[stringed]!!
            println(i)
            println(cycleLength)
        } else {
            map[stringed] = i
            i++
        }
    }
    var extra = (x-i) % cycleLength - 1
    if (extra < 0 ) {
        extra += cycleLength
    }
    repeat(extra){
        result = oneCycle(result)
    }
    //println("-------------------------------------------------")
    //printPattern(result)
    println(calculateNorthLoad(result))
}

fun cycleXTimes(input: List<List<Roeck>>, x: Int): Int {
    var result = input
    repeat(x) {
        result = oneCycle(result)
    }
    return calculateNorthLoad(result)
}

fun oneCycle(input: List<List<Roeck>>): List<List<Roeck>> {
    var result = tiltNorth(input) // north
    result = tiltWest(result) // west
    result = tiltNorth(result.reversed()).reversed() // south
    result = tiltWest(result.map { it.reversed() }).map { it.reversed() }
    return result
}