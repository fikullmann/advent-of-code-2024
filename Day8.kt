import java.io.File

fun main() {
    day8Part2(File("input/8"))
}

fun day8Part1(content: File)
{
    val lines = content.readLines()
    val instructions = lines[0]

    val pairs = mutableMapOf<String, Pair<String, String>>()
    lines.drop(2).sorted().forEach { str ->
        val parsedLine = str.dropLast(1).split(" = (", ", ")
        pairs[parsedLine[0]] = (parsedLine[1] to parsedLine[2])
    }
    var found = false
    var currKey = "AAA"
    var steps = 0
    while (!found)
    {
        for (i in instructions)
        {
            steps++
            if (i == 'L')
            {
                currKey = pairs[currKey]?.first!!
            }
            else if (i == 'R')
            {
                currKey = pairs[currKey]?.second!!
            }
            println(currKey)
            if (currKey == "ZZZ")
            {
                found = true
                break
            }
        }
    }
    println(steps)
}

fun day8Part2(content: File) {
    val lines = content.readLines()
    val instructions = lines[0]
    val pairs = mutableMapOf<String, Pair<String, String>>()
    lines.drop(2).sorted().forEach { str ->
        val parsedLine = str.dropLast(1).split(" = (", ", ")
        pairs[parsedLine[0]] = (parsedLine[1] to parsedLine[2])
    }
    val currKeyList = pairs.keys.filter { it.last() == 'A' }.toMutableList()
    val stepList = mutableListOf<Long>()
    currKeyList.forEach() { it ->
        var foundLoop = false
        var currKey = it
        var steps = 0
        val foundEnds = mutableListOf<String>()
        while (!foundLoop) {
            for (i in instructions) {
                steps++
                if (i == 'L') {
                    currKey = pairs[currKey]?.first!!
                } else if (i == 'R') {
                    currKey = pairs[currKey]?.second!!
                }
                if (currKey.last() == 'Z') {
                    if (foundEnds.contains(currKey)) {
                        foundLoop = true
                        break
                    }
                    foundEnds.add(currKey)
                    foundLoop = true
                    println(steps)
                    stepList.add(steps.toLong())
                    break
                }
            }
        }
    }
    println(lcm(stepList))

    //var steps = 0
    /*while (!found) {
        for (i in instructions) {
            steps++
            currKey.forEachIndexed {idx, currKeyStr ->
                if (i == 'L') {
                    currKey[idx] = pairs[currKeyStr]?.first!!
                } else if (i == 'R') {
                    currKey[idx] = pairs[currKeyStr]?.second!!
                }
            }
            if (currKey.all { it.last() == 'Z' }) {
                found = true
                break
            }
        }
    }
    println(steps)

     */
}

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

private fun gcd(input: List<Long>): Long {
    var result = input[0]
    for (i in 1 until input.size) result = gcd(result, input[i])
    return result
}

private fun lcm(a: Long, b: Long): Long {
    return a * (b / gcd(a, b))
}

private fun lcm(input: List<Long>): Long {
    var result = input[0]
    for (i in 1 until input.size) result = lcm(result, input[i])
    return result
}